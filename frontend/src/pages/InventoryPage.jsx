import React, { useState, useEffect } from 'react';
import api from '../api/api';
import { useAuth } from '../context/AuthContext';
import { 
  Boxes, 
  ArrowUpRight, 
  ArrowDownLeft, 
  RefreshCw, 
  SlidersHorizontal,
  PlusCircle,
  X
} from 'lucide-react';

const InventoryPage = () => {
  const { user } = useAuth();
  const isAdmin = user?.role === 'ADMIN';

  const [movements, setMovements] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  // Adjustment Modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedProductId, setSelectedProductId] = useState('');
  const [adjustmentQty, setAdjustmentQty] = useState('');
  const [reason, setReason] = useState('Stock physical audit correction');
  const [errorMsg, setErrorMsg] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  useEffect(() => {
    fetchStockData();
  }, []);

  const fetchStockData = async () => {
    try {
      setLoading(true);
      const [movRes, prodRes] = await Promise.all([
        api.get('/inventory/movements'),
        api.get('/products?size=100')
      ]);
      setMovements(movRes.data || []);
      setProducts(prodRes.data?.content || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleAdjustStock = async (e) => {
    e.preventDefault();
    if (!selectedProductId || !adjustmentQty) return;

    try {
      await api.post(`/inventory/adjust?productId=${selectedProductId}&quantity=${adjustmentQty}&reason=${encodeURIComponent(reason)}`);
      setSuccessMsg('Stock adjusted successfully!');
      setIsModalOpen(false);
      setSelectedProductId('');
      setAdjustmentQty('');
      fetchStockData();
      setTimeout(() => setSuccessMsg(''), 3000);
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Failed to adjust stock!');
    }
  };

  return (
    <div className="space-y-6">
      {/* Top Banner */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl p-6 border border-slate-100 dark:border-slate-700 shadow-sm flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800 dark:text-slate-100 flex items-center gap-2">
            <Boxes className="w-7 h-7 text-emerald-600" />
            Stock Movements & Audit Logs
          </h1>
          <p className="text-slate-500 dark:text-slate-400 text-sm mt-1">
            Real-time audit trail of every SALE, PURCHASE, RETURN and MANUAL ADJUSTMENT
          </p>
        </div>

        {isAdmin && (
          <button
            onClick={() => setIsModalOpen(true)}
            className="bg-emerald-600 hover:bg-emerald-700 text-white font-semibold px-5 py-2.5 rounded-xl shadow transition-colors flex items-center gap-2 text-sm"
          >
            <SlidersHorizontal className="w-4 h-4" />
            Adjust Stock (Admin Only)
          </button>
        )}
      </div>

      {successMsg && (
        <div className="bg-emerald-50 text-emerald-700 p-4 rounded-xl font-medium border border-emerald-200">
          ✅ {successMsg}
        </div>
      )}

      {/* Stock Movement Log Table */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm overflow-hidden">
        <div className="p-4 border-b border-slate-100 dark:border-slate-700 flex justify-between items-center">
          <h2 className="font-bold text-slate-800 dark:text-slate-100 text-base">Recent Stock Activity</h2>
          <button
            onClick={fetchStockData}
            className="text-xs text-emerald-600 hover:text-emerald-700 font-semibold flex items-center gap-1"
          >
            <RefreshCw className="w-3.5 h-3.5" />
            Refresh Log
          </button>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead className="text-xs uppercase bg-slate-50 dark:bg-slate-700/50 text-slate-500 dark:text-slate-400">
              <tr>
                <th className="px-4 py-3.5">Timestamp</th>
                <th className="px-4 py-3.5">Product</th>
                <th className="px-4 py-3.5">Movement Type</th>
                <th className="px-4 py-3.5 text-center">Change Qty</th>
                <th className="px-4 py-3.5 text-center">Remaining Stock</th>
                <th className="px-4 py-3.5">Reason / Reference</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100 dark:divide-slate-700">
              {loading ? (
                <tr>
                  <td colSpan="6" className="py-12 text-center text-slate-400">Loading stock audit logs...</td>
                </tr>
              ) : movements.length === 0 ? (
                <tr>
                  <td colSpan="6" className="py-12 text-center text-slate-400">No stock movements recorded yet.</td>
                </tr>
              ) : (
                movements.map((mov) => {
                  const isPositive = mov.quantity > 0;
                  return (
                    <tr key={mov.id} className="hover:bg-slate-50/50 dark:hover:bg-slate-700/30">
                      <td className="px-4 py-3 text-xs text-slate-400 font-mono">
                        {new Date(mov.createdAt).toLocaleString('en-IN')}
                      </td>
                      <td className="px-4 py-3 font-semibold text-slate-800 dark:text-slate-200">
                        {mov.product?.name}
                      </td>
                      <td className="px-4 py-3">
                        <span className={`px-2.5 py-1 rounded-full text-xs font-bold ${
                          mov.movementType === 'SALE' ? 'bg-amber-100 text-amber-800' :
                          mov.movementType === 'PURCHASE' ? 'bg-emerald-100 text-emerald-800' :
                          mov.movementType === 'SALES_RETURN' ? 'bg-blue-100 text-blue-800' :
                          'bg-purple-100 text-purple-800'
                        }`}>
                          {mov.movementType}
                        </span>
                      </td>
                      <td className={`px-4 py-3 text-center font-bold ${isPositive ? 'text-emerald-600' : 'text-rose-600'}`}>
                        {isPositive ? `+${mov.quantity}` : mov.quantity}
                      </td>
                      <td className="px-4 py-3 text-center font-semibold text-slate-700 dark:text-slate-300">
                        {mov.remainingStock}
                      </td>
                      <td className="px-4 py-3 text-xs text-slate-500">
                        {mov.reason || 'N/A'}
                      </td>
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Manual Stock Adjustment Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 z-50 bg-slate-900/60 backdrop-blur-sm flex justify-center items-center p-4">
          <div className="bg-white dark:bg-slate-800 rounded-2xl max-w-md w-full p-6 shadow-2xl space-y-4">
            <div className="flex justify-between items-center border-b border-slate-100 dark:border-slate-700 pb-3">
              <h3 className="font-bold text-lg text-slate-800 dark:text-slate-100">Manual Stock Adjustment</h3>
              <button onClick={() => setIsModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            {errorMsg && <div className="p-3 bg-rose-50 text-rose-700 text-xs font-semibold rounded-xl">{errorMsg}</div>}

            <form onSubmit={handleAdjustStock} className="space-y-4 text-sm">
              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Select Product</label>
                <select
                  required
                  value={selectedProductId}
                  onChange={(e) => setSelectedProductId(e.target.value)}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700"
                >
                  <option value="">-- Choose Product --</option>
                  {products.map((p) => (
                    <option key={p.id} value={p.id}>
                      {p.name} (Current Stock: {p.currentStock})
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">
                  Adjustment Quantity (+ for addition, - for reduction/damage)
                </label>
                <input
                  type="number"
                  required
                  placeholder="e.g. +10 or -5"
                  value={adjustmentQty}
                  onChange={(e) => setAdjustmentQty(e.target.value)}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 font-bold"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Audit Reason / Note</label>
                <input
                  type="text"
                  required
                  value={reason}
                  onChange={(e) => setReason(e.target.value)}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700"
                />
              </div>

              <div className="pt-3 border-t border-slate-100 dark:border-slate-700 flex justify-end gap-2">
                <button
                  type="button"
                  onClick={() => setIsModalOpen(false)}
                  className="px-4 py-2 bg-slate-100 text-slate-700 rounded-xl font-semibold"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-5 py-2 bg-emerald-600 text-white rounded-xl font-semibold shadow"
                >
                  Commit Adjustment
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default InventoryPage;
