import React, { useState, useEffect } from 'react';
import api from '../api/api';
import { 
  Truck, 
  Plus, 
  Search, 
  CheckCircle, 
  Phone, 
  X,
  ShoppingBag
} from 'lucide-react';

const SuppliersPage = () => {
  const [suppliers, setSuppliers] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  // Modals
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isPurchaseModalOpen, setIsPurchaseModalOpen] = useState(false);
  const [selectedSupplier, setSelectedSupplier] = useState(null);

  // Supplier Form
  const [formData, setFormData] = useState({ name: '', contactPerson: '', phone: '', email: '', gstin: '', address: '' });
  
  // Purchase Form
  const [purchaseItem, setPurchaseItem] = useState({ productId: '', quantity: '10', purchasePrice: '' });
  const [paymentAmount, setPaymentAmount] = useState('0');

  const [errorMsg, setErrorMsg] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  useEffect(() => {
    fetchInitialData();
  }, []);

  const fetchInitialData = async () => {
    try {
      setLoading(true);
      const [supRes, prodRes] = await Promise.all([
        api.get('/suppliers'),
        api.get('/products?size=100')
      ]);
      setSuppliers(supRes.data?.content || supRes.data || []);
      setProducts(prodRes.data?.content || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateSupplier = async (e) => {
    e.preventDefault();
    try {
      await api.post('/suppliers', formData);
      setSuccessMsg('Supplier registered successfully!');
      setIsAddModalOpen(false);
      setFormData({ name: '', contactPerson: '', phone: '', email: '', gstin: '', address: '' });
      fetchInitialData();
      setTimeout(() => setSuccessMsg(''), 3000);
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Failed to add supplier!');
    }
  };

  const handleCreatePurchase = async (e) => {
    e.preventDefault();
    if (!selectedSupplier || !purchaseItem.productId) return;

    try {
      const selectedProd = products.find(p => p.id === Number(purchaseItem.productId));
      const pPrice = Number(purchaseItem.purchasePrice) || selectedProd?.purchasePrice || 0;
      const qty = Number(purchaseItem.quantity);

      const payload = {
        supplierId: selectedSupplier.id,
        invoiceNumber: 'PUR-' + Date.now().toString().slice(-6),
        items: [
          {
            productId: selectedProd.id,
            quantity: qty,
            purchasePrice: pPrice
          }
        ],
        paidAmount: Number(paymentAmount) || 0,
        notes: 'Stock Intake from Wholesale Supplier'
      };

      await api.post('/purchases', payload);
      setSuccessMsg('Stock intake recorded successfully! Product stock updated.');
      setIsPurchaseModalOpen(false);
      setPurchaseItem({ productId: '', quantity: '10', purchasePrice: '' });
      fetchInitialData();
      setTimeout(() => setSuccessMsg(''), 3000);
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Failed to record stock intake!');
    }
  };

  const formatCurrency = (val) => {
    return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(val || 0);
  };

  return (
    <div className="space-y-6">
      {/* Banner */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl p-6 border border-slate-100 dark:border-slate-700 shadow-sm flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800 dark:text-slate-100 flex items-center gap-2">
            <Truck className="w-7 h-7 text-emerald-600" />
            Wholesale Suppliers & Purchase Orders
          </h1>
          <p className="text-slate-500 dark:text-slate-400 text-sm mt-1">Manage vendor accounts, record bulk stock intakes & payable balances</p>
        </div>

        <button
          onClick={() => setIsAddModalOpen(true)}
          className="bg-emerald-600 hover:bg-emerald-700 text-white font-semibold px-5 py-2.5 rounded-xl shadow transition-colors flex items-center gap-2 text-sm"
        >
          <Plus className="w-4 h-4" />
          Add Supplier
        </button>
      </div>

      {successMsg && (
        <div className="bg-emerald-50 text-emerald-700 p-4 rounded-xl font-medium border border-emerald-200">
          ✅ {successMsg}
        </div>
      )}

      {/* Supplier Directory Table */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead className="text-xs uppercase bg-slate-50 dark:bg-slate-700/50 text-slate-500 dark:text-slate-400">
              <tr>
                <th className="px-4 py-3.5">Supplier / Company Name</th>
                <th className="px-4 py-3.5">Contact Person</th>
                <th className="px-4 py-3.5">GSTIN</th>
                <th className="px-4 py-3.5">Phone</th>
                <th className="px-4 py-3.5 text-right">Payable Balance</th>
                <th className="px-4 py-3.5 text-center">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100 dark:divide-slate-700">
              {loading ? (
                <tr>
                  <td colSpan="6" className="py-12 text-center text-slate-400">Loading supplier accounts...</td>
                </tr>
              ) : suppliers.length === 0 ? (
                <tr>
                  <td colSpan="6" className="py-12 text-center text-slate-400">No suppliers registered.</td>
                </tr>
              ) : (
                suppliers.map((sup) => (
                  <tr key={sup.id} className="hover:bg-slate-50/50 dark:hover:bg-slate-700/30">
                    <td className="px-4 py-3 font-bold text-slate-800 dark:text-slate-100">
                      {sup.name}
                    </td>
                    <td className="px-4 py-3 text-slate-600 dark:text-slate-300">
                      {sup.contactPerson || 'N/A'}
                    </td>
                    <td className="px-4 py-3 text-slate-500 font-mono text-xs">
                      {sup.gstin || 'Unregistered'}
                    </td>
                    <td className="px-4 py-3 text-slate-600 dark:text-slate-300 font-mono text-xs">
                      {sup.phone || 'N/A'}
                    </td>
                    <td className="px-4 py-3 text-right">
                      <span className="px-2.5 py-1 rounded-full text-xs font-bold bg-rose-100 text-rose-800 dark:bg-rose-950 dark:text-rose-300">
                        {formatCurrency(sup.outstandingBalance)}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-center">
                      <button
                        onClick={() => {
                          setSelectedSupplier(sup);
                          setIsPurchaseModalOpen(true);
                        }}
                        className="px-3 py-1.5 bg-emerald-50 hover:bg-emerald-100 text-emerald-700 rounded-lg text-xs font-semibold transition-colors flex items-center gap-1 inline-flex"
                      >
                        <ShoppingBag className="w-3.5 h-3.5" />
                        Record Stock Intake
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add Supplier Modal */}
      {isAddModalOpen && (
        <div className="fixed inset-0 z-50 bg-slate-900/60 backdrop-blur-sm flex justify-center items-center p-4">
          <div className="bg-white dark:bg-slate-800 rounded-2xl max-w-md w-full p-6 shadow-2xl space-y-4">
            <div className="flex justify-between items-center border-b border-slate-100 dark:border-slate-700 pb-3">
              <h3 className="font-bold text-lg text-slate-800 dark:text-slate-100">Add Supplier</h3>
              <button onClick={() => setIsAddModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            {errorMsg && <div className="p-3 bg-rose-50 text-rose-700 text-xs font-semibold rounded-xl">{errorMsg}</div>}

            <form onSubmit={handleCreateSupplier} className="space-y-4 text-sm">
              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Company / Supplier Name</label>
                <input
                  type="text"
                  required
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Contact Person</label>
                <input
                  type="text"
                  value={formData.contactPerson}
                  onChange={(e) => setFormData({ ...formData, contactPerson: e.target.value })}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Mobile Phone</label>
                <input
                  type="text"
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 font-mono"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">GSTIN</label>
                <input
                  type="text"
                  value={formData.gstin}
                  onChange={(e) => setFormData({ ...formData, gstin: e.target.value })}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 font-mono"
                />
              </div>

              <div className="pt-3 border-t border-slate-100 dark:border-slate-700 flex justify-end gap-2">
                <button
                  type="button"
                  onClick={() => setIsAddModalOpen(false)}
                  className="px-4 py-2 bg-slate-100 text-slate-700 rounded-xl font-semibold"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-5 py-2 bg-emerald-600 text-white rounded-xl font-semibold shadow"
                >
                  Save Supplier
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Record Stock Intake Modal */}
      {isPurchaseModalOpen && selectedSupplier && (
        <div className="fixed inset-0 z-50 bg-slate-900/60 backdrop-blur-sm flex justify-center items-center p-4">
          <div className="bg-white dark:bg-slate-800 rounded-2xl max-w-lg w-full p-6 shadow-2xl space-y-4">
            <div className="flex justify-between items-center border-b border-slate-100 dark:border-slate-700 pb-3">
              <h3 className="font-bold text-lg text-slate-800 dark:text-slate-100">Record Stock Intake</h3>
              <button onClick={() => setIsPurchaseModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            <div className="p-3 bg-slate-50 dark:bg-slate-700 rounded-xl text-xs space-y-1">
              <div className="font-bold text-slate-800 dark:text-slate-100">Supplier: {selectedSupplier.name}</div>
            </div>

            <form onSubmit={handleCreatePurchase} className="space-y-4 text-sm">
              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Select Product Intake</label>
                <select
                  required
                  value={purchaseItem.productId}
                  onChange={(e) => {
                    const pid = e.target.value;
                    const p = products.find(x => x.id === Number(pid));
                    setPurchaseItem({
                      ...purchaseItem,
                      productId: pid,
                      purchasePrice: p?.purchasePrice || ''
                    });
                  }}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700"
                >
                  <option value="">-- Select Product --</option>
                  {products.map((prod) => (
                    <option key={prod.id} value={prod.id}>
                      {prod.name} (Current Stock: {prod.currentStock})
                    </option>
                  ))}
                </select>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Received Quantity</label>
                  <input
                    type="number"
                    required
                    min="1"
                    value={purchaseItem.quantity}
                    onChange={(e) => setPurchaseItem({ ...purchaseItem, quantity: e.target.value })}
                    className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 font-bold"
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Unit Cost Price (₹)</label>
                  <input
                    type="number"
                    step="0.01"
                    required
                    value={purchaseItem.purchasePrice}
                    onChange={(e) => setPurchaseItem({ ...purchaseItem, purchasePrice: e.target.value })}
                    className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 font-bold"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Amount Paid Immediately (₹)</label>
                <input
                  type="number"
                  step="0.01"
                  value={paymentAmount}
                  onChange={(e) => setPaymentAmount(e.target.value)}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700"
                />
              </div>

              <div className="pt-3 border-t border-slate-100 dark:border-slate-700 flex justify-end gap-2">
                <button
                  type="button"
                  onClick={() => setIsPurchaseModalOpen(false)}
                  className="px-4 py-2 bg-slate-100 text-slate-700 rounded-xl font-semibold"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-5 py-2 bg-emerald-600 text-white rounded-xl font-semibold shadow"
                >
                  Save & Update Stock
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default SuppliersPage;
