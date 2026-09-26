import React, { useState, useEffect } from 'react';
import { X, Printer, Download, FileText, CheckCircle2 } from 'lucide-react';
import api from '../api/api';

const InvoiceModal = ({ saleId, onClose }) => {
  const [sale, setSale] = useState(null);
  const [loading, setLoading] = useState(true);
  const [pdfUrl, setPdfUrl] = useState(null);

  useEffect(() => {
    if (!saleId) return;

    const fetchInvoice = async () => {
      try {
        setLoading(true);
        const saleRes = await api.get(`/billing/${saleId}`);
        setSale(saleRes.data);

        // Fetch PDF Blob
        const pdfRes = await api.get(`/billing/${saleId}/pdf`, {
          responseType: 'blob'
        });

        const blob = new Blob([pdfRes.data], { type: 'application/pdf' });
        const url = URL.createObjectURL(blob);
        setPdfUrl(url);
      } catch (err) {
        console.error('Failed to load invoice', err);
      } finally {
        setLoading(false);
      }
    };

    fetchInvoice();

    return () => {
      if (pdfUrl) URL.revokeObjectURL(pdfUrl);
    };
  }, [saleId]);

  if (!saleId) return null;

  return (
    <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
      <div className="bg-slate-900 border border-slate-800 rounded-2xl w-full max-w-4xl max-h-[90vh] flex flex-col shadow-2xl overflow-hidden">
        {/* Modal Header */}
        <div className="px-6 py-4 border-b border-slate-800 flex items-center justify-between bg-slate-900/50">
          <div className="flex items-center space-x-3">
            <div className="w-9 h-9 rounded-xl bg-emerald-500/15 border border-emerald-500/30 flex items-center justify-center text-emerald-400">
              <CheckCircle2 className="w-5 h-5" />
            </div>
            <div>
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                Sale Completed - Bill #{sale?.billNumber || '...'}
              </h3>
              <p className="text-xs text-slate-400">PDF Invoice Generated via Backend OpenPDF Engine</p>
            </div>
          </div>

          <div className="flex items-center space-x-3">
            {pdfUrl && (
              <a
                href={pdfUrl}
                download={`Invoice-${sale?.billNumber}.pdf`}
                className="flex items-center space-x-1.5 bg-emerald-600 hover:bg-emerald-500 text-white text-xs px-3.5 py-2 rounded-xl font-medium transition shadow-lg shadow-emerald-600/20"
              >
                <Download className="w-4 h-4" />
                <span>Download PDF</span>
              </a>
            )}
            <button
              onClick={onClose}
              className="p-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-400 hover:text-white transition"
            >
              <X className="w-5 h-5" />
            </button>
          </div>
        </div>

        {/* Modal Body */}
        <div className="flex-1 p-6 overflow-y-auto bg-slate-950 flex flex-col items-center justify-center">
          {loading ? (
            <div className="py-16 text-center space-y-3">
              <div className="w-10 h-10 border-4 border-emerald-500/30 border-t-emerald-500 rounded-full animate-spin mx-auto"></div>
              <p className="text-sm text-slate-400">Generating Store Invoice PDF...</p>
            </div>
          ) : pdfUrl ? (
            <iframe
              src={pdfUrl}
              title="Invoice PDF"
              className="w-full h-[600px] rounded-xl border border-slate-800 bg-white"
            />
          ) : (
            <div className="py-12 text-center text-rose-400">
              <p>Failed to display PDF preview. You can still view details in Sales History.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default InvoiceModal;
