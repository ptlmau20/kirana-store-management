import React, { useState } from 'react';
import api from '../api/api';
import { 
  RotateCcw, 
  CheckCircle, 
  AlertCircle,
  ShoppingBag,
  Truck
} from 'lucide-react';

const ReturnsPage = () => {
  const [returnType, setReturnType] = useState('SALES'); // SALES or PURCHASE
  const [invoiceNumber, setInvoiceNumber] = useState('');
  const [returnReason, setReturnReason] = useState('Customer returned damaged packaging');
  const [refundAmount, setRefundAmount] = useState('');
  
  const [successMsg, setSuccessMsg] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmitReturn = async (e) => {
    e.preventDefault();
    setErrorMsg('');
    setSuccessMsg('');

    if (!invoiceNumber.trim()) {
      setErrorMsg('Please enter a valid Invoice Number!');
      return;
    }

    try {
      setLoading(true);
      if (returnType === 'SALES') {
        const payload = {
          saleInvoiceNumber: invoiceNumber.trim(),
          refundAmount: Number(refundAmount) || 0,
          reason: returnReason,
          items: []
        };
        await api.post('/returns/sales', payload);
        setSuccessMsg(`Sales Return recorded for Invoice "${invoiceNumber}". Stock restored.`);
      } else {
        const payload = {
          purchaseInvoiceNumber: invoiceNumber.trim(),
          refundAmount: Number(refundAmount) || 0,
          reason: returnReason,
          items: []
        };
        await api.post('/returns/purchases', payload);
        setSuccessMsg(`Purchase Return recorded for Supplier Invoice "${invoiceNumber}". Stock reduced.`);
      }
      setInvoiceNumber('');
      setRefundAmount('');
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Failed to process return!');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6 max-w-3xl mx-auto">
      {/* Banner */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl p-6 border border-slate-100 dark:border-slate-700 shadow-sm">
        <h1 className="text-2xl font-bold text-slate-800 dark:text-slate-100 flex items-center gap-2">
          <RotateCcw className="w-7 h-7 text-emerald-600" />
          Stock Returns Management
        </h1>
        <p className="text-slate-500 dark:text-slate-400 text-sm mt-1">Process customer Sales Returns or vendor Purchase Returns with automatic stock reversal</p>
      </div>

      {successMsg && (
        <div className="bg-emerald-50 text-emerald-700 p-4 rounded-xl font-medium border border-emerald-200 flex items-center gap-2">
          <CheckCircle className="w-5 h-5" />
          {successMsg}
        </div>
      )}

      {errorMsg && (
        <div className="bg-rose-50 text-rose-700 p-4 rounded-xl font-medium border border-rose-200 flex items-center gap-2">
          <AlertCircle className="w-5 h-5" />
          {errorMsg}
        </div>
      )}

      {/* Return Type Selector */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl p-6 border border-slate-100 dark:border-slate-700 shadow-sm space-y-6">
        <div className="grid grid-cols-2 gap-3">
          <button
            type="button"
            onClick={() => setReturnType('SALES')}
            className={`py-3.5 px-4 rounded-xl font-bold text-sm flex items-center justify-center gap-2 border transition-all ${
              returnType === 'SALES'
                ? 'bg-emerald-600 text-white border-emerald-600 shadow-md'
                : 'bg-slate-50 dark:bg-slate-700 text-slate-700 dark:text-slate-300 border-slate-200 dark:border-slate-600'
            }`}
          >
            <ShoppingBag className="w-4 h-4" />
            Customer Sales Return (Inward Stock)
          </button>
          
          <button
            type="button"
            onClick={() => setReturnType('PURCHASE')}
            className={`py-3.5 px-4 rounded-xl font-bold text-sm flex items-center justify-center gap-2 border transition-all ${
              returnType === 'PURCHASE'
                ? 'bg-emerald-600 text-white border-emerald-600 shadow-md'
                : 'bg-slate-50 dark:bg-slate-700 text-slate-700 dark:text-slate-300 border-slate-200 dark:border-slate-600'
            }`}
          >
            <Truck className="w-4 h-4" />
            Supplier Purchase Return (Outward Stock)
          </button>
        </div>

        <form onSubmit={handleSubmitReturn} className="space-y-4 text-sm">
          <div>
            <label className="block text-xs font-bold text-slate-500 uppercase mb-1">
              {returnType === 'SALES' ? 'Sales Bill Invoice Number' : 'Supplier Purchase Invoice Number'}
            </label>
            <input
              type="text"
              required
              placeholder={returnType === 'SALES' ? 'e.g. INV-10001' : 'e.g. PUR-10001'}
              value={invoiceNumber}
              onChange={(e) => setInvoiceNumber(e.target.value)}
              className="w-full px-4 py-2.5 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 font-mono text-base font-bold"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Reason for Return</label>
            <input
              type="text"
              required
              value={returnReason}
              onChange={(e) => setReturnReason(e.target.value)}
              className="w-full px-4 py-2.5 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-500 uppercase mb-1">
              {returnType === 'SALES' ? 'Refund Paid to Customer (₹)' : 'Credit Received from Supplier (₹)'}
            </label>
            <input
              type="number"
              step="0.01"
              placeholder="e.g. 150"
              value={refundAmount}
              onChange={(e) => setRefundAmount(e.target.value)}
              className="w-full px-4 py-2.5 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 font-bold"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full py-3.5 bg-emerald-600 hover:bg-emerald-700 text-white font-bold rounded-xl shadow-lg transition-all"
          >
            {loading ? 'Processing Return...' : 'Submit Return Record'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default ReturnsPage;
