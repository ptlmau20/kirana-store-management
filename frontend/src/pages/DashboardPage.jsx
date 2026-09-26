import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/api';
import { 
  TrendingUp, 
  ShoppingBag, 
  AlertTriangle, 
  CreditCard, 
  Truck, 
  PlusCircle, 
  ArrowRight, 
  Receipt,
  Download,
  Users
} from 'lucide-react';
import InvoiceModal from '../components/InvoiceModal';

const DashboardPage = () => {
  const [stats, setStats] = useState(null);
  const [lowStock, setLowStock] = useState([]);
  const [recentSales, setRecentSales] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedSaleId, setSelectedSaleId] = useState(null);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      const statsRes = await api.get('/reports/dashboard');
      setStats(statsRes.data);
      setLowStock(statsRes.data.lowStockProducts || []);
      setRecentSales(statsRes.data.recentSales || []);
    } catch (err) {
      console.error('Failed to load dashboard data', err);
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (val) => {
    return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(val || 0);
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-emerald-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Top Banner */}
      <div className="bg-gradient-to-r from-emerald-600 to-teal-700 rounded-2xl p-6 text-white shadow-lg flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold">Store Dashboard</h1>
          <p className="text-emerald-100 text-sm mt-1">Real-time overview of billing, inventory & outstanding balances</p>
        </div>
        <Link 
          to="/pos"
          className="bg-white text-emerald-700 hover:bg-emerald-50 px-5 py-2.5 rounded-xl font-semibold shadow flex items-center gap-2 transition-all transform active:scale-95"
        >
          <PlusCircle className="w-5 h-5" />
          Open Billing POS (F2)
        </Link>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
        <div className="bg-white dark:bg-slate-800 p-5 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <span className="text-sm font-medium text-slate-500 dark:text-slate-400">Today's Sales</span>
            <div className="p-2.5 bg-emerald-50 dark:bg-emerald-950/50 text-emerald-600 rounded-xl">
              <TrendingUp className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-4">
            <h3 className="text-2xl font-bold text-slate-800 dark:text-slate-100">{formatCurrency(stats?.todaySalesAmount)}</h3>
            <p className="text-xs text-slate-400 mt-1">{stats?.todayOrdersCount || 0} bills generated today</p>
          </div>
        </div>

        <div className="bg-white dark:bg-slate-800 p-5 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <span className="text-sm font-medium text-slate-500 dark:text-slate-400">Total Products</span>
            <div className="p-2.5 bg-blue-50 dark:bg-blue-950/50 text-blue-600 rounded-xl">
              <ShoppingBag className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-4">
            <h3 className="text-2xl font-bold text-slate-800 dark:text-slate-100">{stats?.totalActiveProducts || 0}</h3>
            <p className="text-xs text-blue-600 dark:text-blue-400 font-medium mt-1">Active items in catalog</p>
          </div>
        </div>

        <div className="bg-white dark:bg-slate-800 p-5 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <span className="text-sm font-medium text-slate-500 dark:text-slate-400">Low Stock Items</span>
            <div className="p-2.5 bg-amber-50 dark:bg-amber-950/50 text-amber-600 rounded-xl">
              <AlertTriangle className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-4">
            <h3 className="text-2xl font-bold text-amber-600 dark:text-amber-400">{stats?.lowStockCount || 0}</h3>
            <p className="text-xs text-amber-600 dark:text-amber-400 mt-1">Below minimum threshold</p>
          </div>
        </div>

        <div className="bg-white dark:bg-slate-800 p-5 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <span className="text-sm font-medium text-slate-500 dark:text-slate-400">Customer Udhar</span>
            <div className="p-2.5 bg-purple-50 dark:bg-purple-950/50 text-purple-600 rounded-xl">
              <CreditCard className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-4">
            <h3 className="text-2xl font-bold text-purple-600 dark:text-purple-400">{formatCurrency(stats?.totalUdharOutstanding)}</h3>
            <p className="text-xs text-slate-400 mt-1">Total pending receivable</p>
          </div>
        </div>

        <div className="bg-white dark:bg-slate-800 p-5 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <span className="text-sm font-medium text-slate-500 dark:text-slate-400">Supplier Payable</span>
            <div className="p-2.5 bg-rose-50 dark:bg-rose-950/50 text-rose-600 rounded-xl">
              <Truck className="w-5 h-5" />
            </div>
          </div>
          <div className="mt-4">
            <h3 className="text-2xl font-bold text-rose-600 dark:text-rose-400">{formatCurrency(stats?.totalSupplierPayables)}</h3>
            <p className="text-xs text-slate-400 mt-1">Total pending to suppliers</p>
          </div>
        </div>
      </div>

      {/* Main Grid: Low Stock Alert & Recent Sales */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        
        {/* Low Stock Alerts */}
        <div className="bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm p-5 lg:col-span-1 flex flex-col">
          <div className="flex items-center justify-between pb-4 border-b border-slate-100 dark:border-slate-700">
            <div className="flex items-center gap-2">
              <AlertTriangle className="w-5 h-5 text-amber-500" />
              <h2 className="font-bold text-slate-800 dark:text-slate-100">Low Stock Warning</h2>
            </div>
            <Link to="/inventory" className="text-xs text-emerald-600 hover:text-emerald-700 font-semibold flex items-center gap-1">
              View All <ArrowRight className="w-3 h-3" />
            </Link>
          </div>

          <div className="divide-y divide-slate-100 dark:divide-slate-700 overflow-y-auto max-h-[350px] mt-2">
            {lowStock.length === 0 ? (
              <div className="py-8 text-center text-slate-400 text-sm">
                🎉 All items are adequately stocked!
              </div>
            ) : (
              lowStock.slice(0, 7).map((item) => (
                <div key={item.id} className="py-3 flex items-center justify-between">
                  <div>
                    <h4 className="text-sm font-semibold text-slate-800 dark:text-slate-200 line-clamp-1">{item.name}</h4>
                    <p className="text-xs text-slate-400">Minimum stock: {item.minStockAlert}</p>
                  </div>
                  <div className="text-right">
                    <span className="inline-block px-2.5 py-1 rounded-full text-xs font-bold bg-amber-100 text-amber-800 dark:bg-amber-950 dark:text-amber-300">
                      {item.stockQuantity} {item.unit}
                    </span>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

        {/* Recent Sales Bills */}
        <div className="bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm p-5 lg:col-span-2 flex flex-col">
          <div className="flex items-center justify-between pb-4 border-b border-slate-100 dark:border-slate-700">
            <div className="flex items-center gap-2">
              <Receipt className="w-5 h-5 text-emerald-600" />
              <h2 className="font-bold text-slate-800 dark:text-slate-100">Recent Sales Invoices</h2>
            </div>
            <Link to="/sales-history" className="text-xs text-emerald-600 hover:text-emerald-700 font-semibold flex items-center gap-1">
              Sales History <ArrowRight className="w-3 h-3" />
            </Link>
          </div>

          <div className="overflow-x-auto mt-2">
            <table className="w-full text-left text-sm">
              <thead className="text-xs uppercase bg-slate-50 dark:bg-slate-700/50 text-slate-500 dark:text-slate-400">
                <tr>
                  <th className="px-4 py-3 rounded-l-lg">Invoice No</th>
                  <th className="px-4 py-3">Customer</th>
                  <th className="px-4 py-3">Amount</th>
                  <th className="px-4 py-3">Payment Mode</th>
                  <th className="px-4 py-3 rounded-r-lg text-right">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 dark:divide-slate-700">
                {recentSales.length === 0 ? (
                  <tr>
                    <td colSpan="5" className="py-8 text-center text-slate-400">No recent sales recorded yet.</td>
                  </tr>
                ) : (
                  recentSales.slice(0, 6).map((sale) => (
                    <tr key={sale.id} className="hover:bg-slate-50/50 dark:hover:bg-slate-700/30 transition-colors">
                      <td className="px-4 py-3 font-semibold text-emerald-600 dark:text-emerald-400">{sale.billNumber}</td>
                      <td className="px-4 py-3 text-slate-700 dark:text-slate-300 font-medium">
                        {sale.customerName || 'Walk-in Customer'}
                      </td>
                      <td className="px-4 py-3 font-bold text-slate-800 dark:text-slate-100">
                        {formatCurrency(sale.netAmount)}
                      </td>
                      <td className="px-4 py-3">
                        <span className={`px-2.5 py-1 rounded-full text-xs font-semibold ${
                          sale.paymentMethod === 'CASH' ? 'bg-emerald-100 text-emerald-800 dark:bg-emerald-950 dark:text-emerald-300' :
                          sale.paymentMethod === 'UPI' ? 'bg-blue-100 text-blue-800 dark:bg-blue-950 dark:text-blue-300' :
                          sale.paymentMethod === 'CREDIT' ? 'bg-purple-100 text-purple-800 dark:bg-purple-950 dark:text-purple-300' :
                          'bg-slate-100 text-slate-800 dark:bg-slate-700 dark:text-slate-300'
                        }`}>
                          {sale.paymentMethod}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-right">
                        <button
                          onClick={() => setSelectedSaleId(sale.id)}
                          className="px-3 py-1 bg-slate-100 dark:bg-slate-700 hover:bg-emerald-100 hover:text-emerald-700 text-slate-600 dark:text-slate-300 rounded-lg text-xs font-semibold transition-colors flex items-center gap-1 inline-flex"
                        >
                          <Receipt className="w-3.5 h-3.5" />
                          View Invoice
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>

      </div>

      {/* Invoice Viewer Modal */}
      {selectedSaleId && (
        <InvoiceModal 
          saleId={selectedSaleId} 
          onClose={() => setSelectedSaleId(null)} 
        />
      )}
    </div>
  );
};

export default DashboardPage;
