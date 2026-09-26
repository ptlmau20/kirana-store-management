import React, { useState, useEffect } from 'react';
import api from '../api/api';
import { useAuth } from '../context/AuthContext';
import { 
  Users, 
  Plus, 
  Search, 
  CreditCard, 
  CheckCircle, 
  Phone, 
  MapPin, 
  X,
  DollarSign,
  Pencil,
  Trash2,
  ArrowUpDown
} from 'lucide-react';

const CustomersPage = () => {
  const { isAdmin } = useAuth();
  const [customers, setCustomers] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [loading, setLoading] = useState(true);

  // Modals
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isPaymentModalOpen, setIsPaymentModalOpen] = useState(false);
  const [isAdjustUdharModalOpen, setIsAdjustUdharModalOpen] = useState(false);
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [editingCustomer, setEditingCustomer] = useState(null);

  // Forms
  const [formData, setFormData] = useState({ name: '', phone: '', email: '', address: '' });
  const [paymentAmount, setPaymentAmount] = useState('');
  const [paymentNotes, setPaymentNotes] = useState('Udhar Repayment');
  const [adjustmentDelta, setAdjustmentDelta] = useState('');
  const [adjustmentReason, setAdjustmentReason] = useState('');
  
  const [errorMsg, setErrorMsg] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    try {
      setLoading(true);
      const res = await api.get('/customers');
      setCustomers(res.data?.content || res.data || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const openAddCustomer = () => {
    setEditingCustomer(null);
    setFormData({ name: '', phone: '', email: '', address: '' });
    setErrorMsg('');
    setIsAddModalOpen(true);
  };

  const openEditCustomer = (customer) => {
    setEditingCustomer(customer);
    setFormData({
      name: customer.name || '',
      phone: customer.phone || '',
      email: customer.email || '',
      address: customer.address || ''
    });
    setErrorMsg('');
    setIsAddModalOpen(true);
  };

  const handleSaveCustomer = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        name: formData.name.trim(),
        phone: formData.phone.trim(),
        email: formData.email.trim() || null,
        address: formData.address.trim() || null
      };
      if (editingCustomer) {
        await api.put(`/customers/${editingCustomer.id}`, payload);
        setSuccessMsg('Customer updated successfully.');
      } else {
        await api.post('/customers', payload);
        setSuccessMsg('Customer added successfully.');
      }
      setIsAddModalOpen(false);
      setEditingCustomer(null);
      setFormData({ name: '', phone: '', email: '', address: '' });
      setErrorMsg('');
      fetchCustomers();
      setTimeout(() => setSuccessMsg(''), 3000);
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Failed to save customer.');
    }
  };

  const handleRecordPayment = async (e) => {
    e.preventDefault();
    if (!selectedCustomer || !paymentAmount) return;
    if (Number(paymentAmount) <= 0 || Number(paymentAmount) > Number(selectedCustomer.creditBalance)) {
      setErrorMsg('Repayment must be greater than zero and cannot exceed the outstanding Udhar.');
      return;
    }

    try {
      await api.post('/customers/payments', {
        customerId: selectedCustomer.id,
        amountPaid: Number(paymentAmount),
        paymentMethod: 'CASH',
        referenceNote: paymentNotes.trim() || 'Udhar repayment'
      });
      setSuccessMsg(`Repayment of ${formatCurrency(paymentAmount)} recorded for ${selectedCustomer.name}.`);
      setIsPaymentModalOpen(false);
      setPaymentAmount('');
      setErrorMsg('');
      fetchCustomers();
      setTimeout(() => setSuccessMsg(''), 3000);
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Failed to record repayment.');
    }
  };

  const handleAdjustUdhar = async (e) => {
    e.preventDefault();
    if (!selectedCustomer || !adjustmentDelta || !adjustmentReason.trim()) return;
    try {
      const res = await api.post(`/customers/${selectedCustomer.id}/udhar-adjustments`, {
        balanceDelta: Number(adjustmentDelta),
        reason: adjustmentReason.trim()
      });
      setSuccessMsg(`Udhar updated. New balance: ${formatCurrency(res.data.creditBalance)}.`);
      setIsAdjustUdharModalOpen(false);
      setAdjustmentDelta('');
      setAdjustmentReason('');
      setErrorMsg('');
      fetchCustomers();
      setTimeout(() => setSuccessMsg(''), 3000);
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Failed to adjust Udhar.');
    }
  };

  const handleDeleteCustomer = async (customer) => {
    if (!window.confirm(`Remove ${customer.name}? Customers with Udhar, bills, or payment history cannot be removed.`)) return;
    try {
      await api.delete(`/customers/${customer.id}`);
      setSuccessMsg(`${customer.name} was removed.`);
      setErrorMsg('');
      fetchCustomers();
      setTimeout(() => setSuccessMsg(''), 3000);
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Customer could not be removed.');
    }
  };

  const formatCurrency = (val) => {
    return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(val || 0);
  };

  const filteredCustomers = customers.filter(c => 
    (c.name || '').toLowerCase().includes(searchQuery.toLowerCase()) || 
    (c.phone && c.phone.includes(searchQuery))
  );

  return (
    <div className="space-y-6">
      {/* Banner */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl p-6 border border-slate-100 dark:border-slate-700 shadow-sm flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800 dark:text-slate-100 flex items-center gap-2">
            <Users className="w-7 h-7 text-emerald-600" />
            Customer & Udhar Khata Management
          </h1>
          <p className="text-slate-500 dark:text-slate-400 text-sm mt-1">Manage customer details, Udhar balances, and repayments</p>
        </div>

        <button
          onClick={openAddCustomer}
          className="bg-emerald-600 hover:bg-emerald-700 text-white font-semibold px-5 py-2.5 rounded-xl shadow transition-colors flex items-center gap-2 text-sm"
        >
          <Plus className="w-4 h-4" />
            Add Customer
        </button>
      </div>

      {successMsg && (
        <div className="bg-emerald-50 text-emerald-700 p-4 rounded-xl font-medium border border-emerald-200">
          ✅ {successMsg}
        </div>
      )}
      {errorMsg && !isAddModalOpen && !isPaymentModalOpen && !isAdjustUdharModalOpen && (
        <div role="alert" className="bg-rose-50 text-rose-700 p-4 rounded-xl font-medium border border-rose-200">
          {errorMsg}
        </div>
      )}

      {/* Search Bar */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl p-4 border border-slate-100 dark:border-slate-700 shadow-sm flex items-center">
        <div className="relative flex-1">
          <Search className="w-5 h-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
          <input
            type="text"
            placeholder="Search customer by name or phone number..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-11 pr-4 py-2.5 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 text-slate-800 dark:text-slate-100 text-sm focus:ring-2 focus:ring-emerald-500"
          />
        </div>
      </div>

      {/* Customer Directory Table */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead className="text-xs uppercase bg-slate-50 dark:bg-slate-700/50 text-slate-500 dark:text-slate-400">
              <tr>
                <th className="px-4 py-3.5">Customer Name</th>
                <th className="px-4 py-3.5">Phone</th>
                <th className="px-4 py-3.5">Address</th>
                <th className="px-4 py-3.5 text-right">Udhar Balance</th>
                <th className="px-4 py-3.5 text-center">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100 dark:divide-slate-700">
              {loading ? (
                <tr>
                  <td colSpan="5" className="py-12 text-center text-slate-400">Loading customers...</td>
                </tr>
              ) : filteredCustomers.length === 0 ? (
                <tr>
                  <td colSpan="5" className="py-12 text-center text-slate-400">No customers found.</td>
                </tr>
              ) : (
                filteredCustomers.map((cust) => {
                  const hasUdhar = Number(cust.creditBalance) > 0;
                  return (
                    <tr key={cust.id} className="hover:bg-slate-50/50 dark:hover:bg-slate-700/30">
                      <td className="px-4 py-3 font-bold text-slate-800 dark:text-slate-100">
                        {cust.name}
                      </td>
                      <td className="px-4 py-3 text-slate-600 dark:text-slate-300 font-mono text-xs">
                        {cust.phone || 'N/A'}
                      </td>
                      <td className="px-4 py-3 text-slate-500 text-xs">
                        {cust.address || 'N/A'}
                      </td>
                      <td className="px-4 py-3 text-right">
                        <span className={`px-2.5 py-1 rounded-full text-xs font-bold ${
                          hasUdhar ? 'bg-purple-100 text-purple-800' : 'bg-emerald-100 text-emerald-800'
                        }`}>
                          {formatCurrency(cust.creditBalance)}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-center">
                        <div className="flex items-center justify-center gap-1">
                          <button
                            onClick={() => {
                              setSelectedCustomer(cust);
                              setPaymentAmount('');
                              setErrorMsg('');
                              setIsPaymentModalOpen(true);
                            }}
                            disabled={!hasUdhar}
                            title="Record repayment"
                            className="p-2 text-emerald-700 hover:bg-emerald-50 rounded-lg disabled:opacity-40 disabled:cursor-not-allowed"
                          >
                            <DollarSign className="w-4 h-4" />
                          </button>
                          {isAdmin && (
                            <button
                              onClick={() => {
                                setSelectedCustomer(cust);
                                setAdjustmentDelta('');
                                setAdjustmentReason('');
                                setErrorMsg('');
                                setIsAdjustUdharModalOpen(true);
                              }}
                              title="Adjust Udhar"
                              className="p-2 text-amber-700 hover:bg-amber-50 rounded-lg"
                            >
                              <ArrowUpDown className="w-4 h-4" />
                            </button>
                          )}
                          <button
                            onClick={() => openEditCustomer(cust)}
                            title="Edit customer"
                            className="p-2 text-slate-500 hover:bg-slate-100 rounded-lg"
                          >
                            <Pencil className="w-4 h-4" />
                          </button>
                          {isAdmin && (
                            <button
                              onClick={() => handleDeleteCustomer(cust)}
                              title="Remove customer"
                              className="p-2 text-rose-600 hover:bg-rose-50 rounded-lg"
                            >
                              <Trash2 className="w-4 h-4" />
                            </button>
                          )}
                        </div>
                      </td>
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add Customer Modal */}
      {isAddModalOpen && (
        <div className="fixed inset-0 z-50 bg-slate-900/60 backdrop-blur-sm flex justify-center items-center p-4">
          <div className="bg-white dark:bg-slate-800 rounded-2xl max-w-md w-full p-6 shadow-2xl space-y-4">
            <div className="flex justify-between items-center border-b border-slate-100 dark:border-slate-700 pb-3">
              <h3 className="font-bold text-lg text-slate-800 dark:text-slate-100">{editingCustomer ? 'Edit Customer' : 'Add New Customer'}</h3>
              <button onClick={() => setIsAddModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            {errorMsg && <div className="p-3 bg-rose-50 text-rose-700 text-xs font-semibold rounded-xl">{errorMsg}</div>}

            <form onSubmit={handleSaveCustomer} className="space-y-4 text-sm">
              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Customer Full Name</label>
                <input
                  type="text"
                  required
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Mobile Number</label>
                <input
                  type="tel"
                  required
                  inputMode="numeric"
                  pattern="[0-9]{10}"
                  maxLength={10}
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value.replace(/\D/g, '') })}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 font-mono"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Email (Optional)</label>
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Address</label>
                <input
                  type="text"
                  value={formData.address}
                  onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700"
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
                  {editingCustomer ? 'Update Customer' : 'Save Customer'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Record Payment Modal */}
      {isPaymentModalOpen && selectedCustomer && (
        <div className="fixed inset-0 z-50 bg-slate-900/60 backdrop-blur-sm flex justify-center items-center p-4">
          <div className="bg-white dark:bg-slate-800 rounded-2xl max-w-md w-full p-6 shadow-2xl space-y-4">
            <div className="flex justify-between items-center border-b border-slate-100 dark:border-slate-700 pb-3">
              <h3 className="font-bold text-lg text-slate-800 dark:text-slate-100">Record Udhar Repayment</h3>
              <button onClick={() => setIsPaymentModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            {errorMsg && <div role="alert" className="p-3 bg-rose-50 text-rose-700 text-xs font-semibold rounded-xl">{errorMsg}</div>}

            <div className="p-3 bg-slate-50 dark:bg-slate-700 rounded-xl space-y-1 text-xs">
              <div className="font-bold text-sm text-slate-800 dark:text-slate-100">{selectedCustomer.name}</div>
              <div className="text-slate-500">Current Outstanding Udhar: <span className="font-bold text-purple-600">{formatCurrency(selectedCustomer.creditBalance)}</span></div>
            </div>

            <form onSubmit={handleRecordPayment} className="space-y-4 text-sm">
              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Repayment Amount Received (₹)</label>
                <input
                  type="number"
                  step="0.01"
                  min="0.01"
                  max={selectedCustomer.creditBalance}
                  required
                  placeholder="e.g. 500"
                  value={paymentAmount}
                  onChange={(e) => setPaymentAmount(e.target.value)}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 font-bold text-base text-emerald-600"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Notes</label>
                <input
                  type="text"
                  value={paymentNotes}
                  onChange={(e) => setPaymentNotes(e.target.value)}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700"
                />
              </div>

              <div className="pt-3 border-t border-slate-100 dark:border-slate-700 flex justify-end gap-2">
                <button
                  type="button"
                  onClick={() => setIsPaymentModalOpen(false)}
                  className="px-4 py-2 bg-slate-100 text-slate-700 rounded-xl font-semibold"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-5 py-2 bg-emerald-600 text-white rounded-xl font-semibold shadow"
                >
                  Confirm Payment
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {isAdjustUdharModalOpen && selectedCustomer && (
        <div className="fixed inset-0 z-50 bg-slate-900/60 backdrop-blur-sm flex justify-center items-center p-4">
          <div className="bg-white dark:bg-slate-800 rounded-2xl max-w-md w-full p-6 shadow-2xl space-y-4">
            <div className="flex justify-between items-center border-b border-slate-100 dark:border-slate-700 pb-3">
              <h3 className="font-bold text-lg text-slate-800 dark:text-slate-100">Adjust Udhar Balance</h3>
              <button onClick={() => setIsAdjustUdharModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                <X className="w-5 h-5" />
              </button>
            </div>
            {errorMsg && <div role="alert" className="p-3 bg-rose-50 text-rose-700 text-xs font-semibold rounded-xl">{errorMsg}</div>}
            <div className="p-3 bg-slate-50 dark:bg-slate-700 rounded-xl text-sm">
              <div className="font-bold text-slate-800 dark:text-slate-100">{selectedCustomer.name}</div>
              <div className="text-slate-500">Current balance: {formatCurrency(selectedCustomer.creditBalance)}</div>
            </div>
            <form onSubmit={handleAdjustUdhar} className="space-y-4 text-sm">
              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Balance Change (₹)</label>
                <input
                  type="number"
                  step="0.01"
                  required
                  value={adjustmentDelta}
                  onChange={(e) => setAdjustmentDelta(e.target.value)}
                  placeholder="Positive adds; negative reduces"
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700"
                />
              </div>
              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Reason</label>
                <input
                  type="text"
                  required
                  maxLength={255}
                  value={adjustmentReason}
                  onChange={(e) => setAdjustmentReason(e.target.value)}
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700"
                />
              </div>
              <div className="pt-3 border-t border-slate-100 dark:border-slate-700 flex justify-end gap-2">
                <button type="button" onClick={() => setIsAdjustUdharModalOpen(false)} className="px-4 py-2 bg-slate-100 text-slate-700 rounded-xl font-semibold">Cancel</button>
                <button type="submit" className="px-5 py-2 bg-emerald-600 text-white rounded-xl font-semibold shadow">Save Adjustment</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default CustomersPage;
