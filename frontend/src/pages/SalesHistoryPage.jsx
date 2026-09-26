import React, { useState, useEffect } from 'react';
import api from '../api/api';
import { useAuth } from '../context/AuthContext';
import { 
  Receipt, 
  Search, 
  Download, 
  ChevronLeft, 
  ChevronRight,
  UserCheck,
  Trash2
} from 'lucide-react';
import InvoiceModal from '../components/InvoiceModal';

const SalesHistoryPage = () => {
  const { isAdmin } = useAuth();
  const [sales, setSales] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [selectedSaleId, setSelectedSaleId] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    fetchSales();
  }, [currentPage]);

  const fetchSales = async () => {
    try {
      setLoading(true);
      const res = await api.get(`/billing/sales?page=${currentPage}&size=15`);
      setSales(res.data?.content || []);
      setTotalPages(res.data?.totalPages || 0);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (val) => {
    return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(val || 0);
  };

  const handleDeleteSale = async (sale) => {
    const confirmed = window.confirm(
      `Permanently delete bill ${sale.billNumber}? Stock will be restored and outstanding Udhar reversed. Any cash/UPI refund must be handled separately.`
    );
    if (!confirmed) return;

    try {
      setErrorMessage('');
      await api.delete(`/billing/${sale.id}`);
      setSuccessMessage(`Bill ${sale.billNumber} was permanently deleted.`);
      fetchSales();
      setTimeout(() => setSuccessMessage(''), 3000);
    } catch (error) {
      setErrorMessage(error.response?.data?.message || 'Could not delete this bill.');
    }
  };

  return (
    <div className="space-y-6">
      {/* Banner */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl p-6 border border-slate-100 dark:border-slate-700 shadow-sm">
        <h1 className="text-2xl font-bold text-slate-800 dark:text-slate-100 flex items-center gap-2">
          <Receipt className="w-7 h-7 text-emerald-600" />
          Sales Invoices & Billing History
        </h1>
        <p className="text-slate-500 dark:text-slate-400 text-sm mt-1">Complete historical log of all customer bills, GST breakdown & payment modes</p>
      </div>

      {successMessage && (
        <div role="status" className="bg-emerald-50 text-emerald-700 p-4 rounded-xl font-medium border border-emerald-200">
          {successMessage}
        </div>
      )}
      {errorMessage && (
        <div role="alert" className="bg-rose-50 text-rose-700 p-4 rounded-xl font-medium border border-rose-200">
          {errorMessage}
        </div>
      )}

      {/* Sales Table */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead className="text-xs uppercase bg-slate-50 dark:bg-slate-700/50 text-slate-500 dark:text-slate-400">
              <tr>
                <th className="px-4 py-3.5">Invoice #</th>
                <th className="px-4 py-3.5">Date & Time</th>
                <th className="px-4 py-3.5">Customer</th>
                <th className="px-4 py-3.5 text-right">Subtotal</th>
                <th className="px-4 py-3.5 text-right">GST Total</th>
                <th className="px-4 py-3.5 text-right">Net Amount</th>
                <th className="px-4 py-3.5 text-center">Payment Mode</th>
                <th className="px-4 py-3.5 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100 dark:divide-slate-700">
              {loading ? (
                <tr>
                  <td colSpan="8" className="py-12 text-center text-slate-400">Loading sales history...</td>
                </tr>
              ) : sales.length === 0 ? (
                <tr>
                  <td colSpan="8" className="py-12 text-center text-slate-400">No sales bills recorded yet.</td>
                </tr>
              ) : (
                sales.map((sale) => (
                  <tr key={sale.id} className="hover:bg-slate-50/50 dark:hover:bg-slate-700/30">
                    <td className="px-4 py-3 font-bold text-emerald-600 dark:text-emerald-400 font-mono">
                      {sale.billNumber}
                    </td>
                    <td className="px-4 py-3 text-xs text-slate-400 font-mono">
                      {new Date(sale.createdAt).toLocaleString('en-IN')}
                    </td>
                    <td className="px-4 py-3 font-medium text-slate-700 dark:text-slate-300">
                      {sale.customerName || 'Walk-in Customer'}
                    </td>
                    <td className="px-4 py-3 text-right text-slate-500">
                      {formatCurrency(sale.totalAmount)}
                    </td>
                    <td className="px-4 py-3 text-right text-slate-500">
                      {formatCurrency(sale.gstAmount)}
                    </td>
                    <td className="px-4 py-3 text-right font-bold text-slate-800 dark:text-slate-100">
                      {formatCurrency(sale.netAmount)}
                    </td>
                    <td className="px-4 py-3 text-center">
                      <span className={`px-2.5 py-1 rounded-full text-xs font-semibold ${
                        sale.paymentMethod === 'CASH' ? 'bg-emerald-100 text-emerald-800' :
                        sale.paymentMethod === 'UPI' ? 'bg-blue-100 text-blue-800' :
                        sale.paymentMethod === 'CREDIT' ? 'bg-purple-100 text-purple-800' :
                        'bg-slate-100 text-slate-800'
                      }`}>
                        {sale.paymentMethod}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-right">
                      <div className="flex items-center justify-end gap-2">
                      <button
                        onClick={() => setSelectedSaleId(sale.id)}
                        className="px-3 py-1 bg-slate-100 dark:bg-slate-700 hover:bg-emerald-100 hover:text-emerald-700 text-slate-700 dark:text-slate-300 rounded-lg text-xs font-semibold transition-colors flex items-center gap-1 inline-flex"
                      >
                        <Receipt className="w-3.5 h-3.5" />
                        View PDF Bill
                      </button>
                      {isAdmin && (
                        <button
                          type="button"
                          onClick={() => handleDeleteSale(sale)}
                          title="Permanently delete sale"
                          aria-label={`Delete bill ${sale.billNumber}`}
                          className="p-2 text-rose-600 hover:bg-rose-50 rounded-lg"
                        >
                          <Trash2 className="w-4 h-4" />
                        </button>
                      )}
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* Pagination Bar */}
        {totalPages > 1 && (
          <div className="p-4 border-t border-slate-100 dark:border-slate-700 flex justify-between items-center">
            <span className="text-xs text-slate-500">
              Page {currentPage + 1} of {totalPages}
            </span>
            <div className="flex items-center gap-2">
              <button
                disabled={currentPage === 0}
                onClick={() => setCurrentPage((p) => p - 1)}
                className="p-2 rounded-xl border border-slate-200 dark:border-slate-700 disabled:opacity-40"
              >
                <ChevronLeft className="w-4 h-4" />
              </button>
              <button
                disabled={currentPage >= totalPages - 1}
                onClick={() => setCurrentPage((p) => p + 1)}
                className="p-2 rounded-xl border border-slate-200 dark:border-slate-700 disabled:opacity-40"
              >
                <ChevronRight className="w-4 h-4" />
              </button>
            </div>
          </div>
        )}
      </div>

      {selectedSaleId && (
        <InvoiceModal
          saleId={selectedSaleId}
          onClose={() => setSelectedSaleId(null)}
        />
      )}
    </div>
  );
};

export default SalesHistoryPage;
