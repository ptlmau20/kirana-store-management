package com.kirana.store.service;

import com.kirana.store.entity.Sale;
import com.kirana.store.entity.SaleItem;
import com.kirana.store.exception.ResourceNotFoundException;
import com.kirana.store.repository.SaleRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.awt.Color;
import java.time.format.DateTimeFormatter;

@Service
public class InvoicePdfService {

    private final SaleRepository saleRepository;
    private final BillingService billingService;

    public InvoicePdfService(SaleRepository saleRepository, BillingService billingService) {
        this.saleRepository = saleRepository;
        this.billingService = billingService;
    }

    @Transactional(readOnly = true)
    public byte[] generateInvoicePdf(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale/Bill not found with id: " + saleId));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.DARK_GRAY);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);

            Paragraph header = new Paragraph("MAHAKALI KIRANA STORE", titleFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Paragraph storeDetails = new Paragraph("Main Market Road, Sector 12, City - 380001 | Phone: +91 98765 43210 | GSTIN: 24AAACK1234F1Z9\n\n", subtitleFont);
            storeDetails.setAlignment(Element.ALIGN_CENTER);
            document.add(storeDetails);

            Paragraph divider = new Paragraph("----------------------------------------------------------------------------------------------------------------------------------\n", subtitleFont);
            document.add(divider);

            PdfPTable metaTable = new PdfPTable(2);
            metaTable.setWidthPercentage(100);
            metaTable.setWidths(new float[]{50f, 50f});

            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.addElement(new Paragraph("INVOICE / BILL DETAILS", sectionFont));
            leftCell.addElement(new Paragraph("Bill Number: " + sale.getBillNumber(), boldFont));
            leftCell.addElement(new Paragraph("Date: " + sale.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")), normalFont));
            leftCell.addElement(new Paragraph("Cashier: " + billingService.resolveCashierName(sale.getCashierName()), normalFont));
            leftCell.addElement(new Paragraph("Payment Method: " + sale.getPaymentMethod().name(), boldFont));
            metaTable.addCell(leftCell);

            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.addElement(new Paragraph("CUSTOMER DETAILS", sectionFont));
            if (sale.getCustomer() != null) {
                rightCell.addElement(new Paragraph("Name: " + sale.getCustomer().getName(), boldFont));
                rightCell.addElement(new Paragraph("Phone: " + sale.getCustomer().getPhone(), normalFont));
                if (sale.getCustomer().getAddress() != null) {
                    rightCell.addElement(new Paragraph("Address: " + sale.getCustomer().getAddress(), normalFont));
                }
            } else {
                rightCell.addElement(new Paragraph("Customer: Walk-in Retail Customer", normalFont));
            }
            metaTable.addCell(rightCell);

            document.add(metaTable);
            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{8f, 35f, 15f, 10f, 12f, 10f, 15f});

            String[] headers = {"#", "Item Description", "HSN", "Qty", "Rate (₹)", "GST %", "Total (₹)"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(new Color(30, 41, 59));
                cell.setPadding(6);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            int index = 1;
            for (SaleItem item : sale.getItems()) {
                table.addCell(createCell(String.valueOf(index++), normalFont, Element.ALIGN_CENTER));
                table.addCell(createCell(item.getProduct().getName(), normalFont, Element.ALIGN_LEFT));
                table.addCell(createCell(item.getProduct().getHsnCode() != null ? item.getProduct().getHsnCode() : "-", normalFont, Element.ALIGN_CENTER));
                table.addCell(createCell(String.valueOf(item.getQuantity()) + " " + item.getProduct().getUnit(), normalFont, Element.ALIGN_CENTER));
                table.addCell(createCell(String.format("%.2f", item.getUnitPrice()), normalFont, Element.ALIGN_RIGHT));
                table.addCell(createCell(String.format("%.1f%%", item.getGstRate()), normalFont, Element.ALIGN_CENTER));
                table.addCell(createCell(String.format("%.2f", item.getItemTotal()), normalFont, Element.ALIGN_RIGHT));
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            PdfPTable totalsTable = new PdfPTable(2);
            totalsTable.setWidthPercentage(45);
            totalsTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            addTotalRow(totalsTable, "Subtotal:", "₹ " + String.format("%.2f", sale.getTotalAmount()), normalFont);
            addTotalRow(totalsTable, "Discount:", "- ₹ " + String.format("%.2f", sale.getDiscountAmount()), normalFont);
            addTotalRow(totalsTable, "Total GST:", "+ ₹ " + String.format("%.2f", sale.getGstAmount()), normalFont);
            addTotalRow(totalsTable, "Grand Total:", "₹ " + String.format("%.2f", sale.getNetAmount()), boldFont);
            addTotalRow(totalsTable, "Amount Paid:", "₹ " + String.format("%.2f", sale.getPaidAmount()), normalFont);
            addTotalRow(totalsTable, "Balance Due:", "₹ " + String.format("%.2f", sale.getDueAmount()), boldFont);

            document.add(totalsTable);

            document.add(new Paragraph("\n\n"));
            Paragraph footer = new Paragraph("Thank you for your business! Please visit again.", subtitleFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF invoice", e);
        }

        return out.toByteArray();
    }

    private PdfPCell createCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    private void addTotalRow(PdfPTable table, String label, String value, Font font) {
        PdfPCell c1 = new PdfPCell(new Phrase(label, font));
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase(value, font));
        c2.setBorder(Rectangle.NO_BORDER);
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c2);
    }
}
