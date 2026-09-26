import React, { useState, useEffect, useRef } from 'react';
import api from '../api/api';
import { 
  Search, 
  Plus, 
  Minus, 
  Trash2, 
  UserCheck, 
  CreditCard, 
  CheckCircle2, 
  Printer, 
  RefreshCw,
  ShoppingBag,
  Tag
} from 'lucide-react';
import InvoiceModal from '../components/InvoiceModal';

const POSPage = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [productsList, setProductsList] = useState([]);
  const [cart, setCart] = useState([]);
  
  // Customer & Payment
  const [customers, setCustomers] = useState([]);
  const [selectedCustomerId, setSelectedCustomerId] = useState('');
  const [customerName, setCustomerName] = useState('');
  const [customerPhone, setCustomerPhone] = useState('');
  const [paymentMode, setPaymentMode] = useState('CASH');
  const [discountAmount, setDiscountAmount] = useState(0);
  const [cashTendered, setCashTendered] = useState('');
  
  // Modals & States
  const [processing, setProcessing] = useState(false);
  const [completedSaleId, setCompletedSaleId] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');

  const searchInputRef = useRef(null);

  useEffect(() => {
    fetchInitialData();
    if (searchInputRef.current) {
      searchInputRef.current.focus();
    }

    const handleKeyDown = (e) => {
      if (e.key === 'F2') {
        e.preventDefault();
        if (searchInputRef.current) searchInputRef.current.focus();
      } else if (e.key === 'F8') {
        e.preventDefault();
        if (cart.length > 0) handleCheckout();
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [cart, selectedCustomerId, paymentMode, discountAmount]);

  const fetchInitialData = async () => {
    try {
      const [catsRes, custsRes, prodsRes] = await Promise.all([
        api.get('/categories'),
        api.get('/customers'),
        api.get('/products')
      ]);
      setCategories(catsRes.data || []);
      setCustomers(custsRes.data?.content || custsRes.data || []);
      setProductsList(Array.isArray(prodsRes.data) ? prodsRes.data : prodsRes.data?.content || []);
    } catch (err) {
      console.error('Failed to load initial POS data', err);
    }
  };

  const handleSearchChange = async (e) => {
    const val = e.target.value;
    setSearchQuery(val);

    if (val.trim().length > 1) {
      try {
        const res = await api.get(`/products?query=${encodeURIComponent(val)}`);
        setSearchResults(res.data || []);
      } catch (err) {
        console.error(err);
      }
    } else {
      setSearchResults([]);
    }
  };

  const addToCart = (product) => {
    if (product.stockQuantity <= 0) {
      setErrorMessage(`"${product.name}" is out of stock!`);
      setTimeout(() => setErrorMessage(''), 3000);
      return;
    }

    setCart((prevCart) => {
      const existingIndex = prevCart.findIndex((item) => item.product.id === product.id);
      if (existingIndex > -1) {
        const updatedCart = [...prevCart];
        const currentQty = updatedCart[existingIndex].quantity;
        if (currentQty + 1 > product.stockQuantity) {
          setErrorMessage(`Cannot add more than available stock (${product.stockQuantity})`);
          setTimeout(() => setErrorMessage(''), 3000);
          return prevCart;
        }
        updatedCart[existingIndex].quantity += 1;
        return updatedCart;
      } else {
        return [
          ...prevCart,
          {
            product,
            quantity: 1,
            unitPrice: product.sellingPrice,
            discountPercentage: 0
          }
        ];
      }
    });

    setSearchQuery('');
    setSearchResults([]);
  };

  const updateQuantity = (productId, delta) => {
    setCart((prevCart) => {
      return prevCart
        .map((item) => {
          if (item.product.id === productId) {
            const newQty = item.quantity + delta;
            if (newQty > item.product.stockQuantity) {
              setErrorMessage(`Stock limit reached for ${item.product.name}`);
              setTimeout(() => setErrorMessage(''), 3000);
              return item;
            }
            return newQty > 0 ? { ...item, quantity: newQty } : null;
          }
          return item;
        })
        .filter(Boolean);
    });
  };

  const removeFromCart = (productId) => {
    setCart((prevCart) => prevCart.filter((item) => item.product.id !== productId));
  };

  const clearCart = () => {
    setCart([]);
    setSelectedCustomerId('');
    setCustomerName('');
    setCustomerPhone('');
    setPaymentMode('CASH');
    setDiscountAmount(0);
    setCashTendered('');
  };

  // Billing Calculations
  const calculateTotals = () => {
    let grossTotal = 0;
    let totalGst = 0;
    let totalDiscount = Number(discountAmount) || 0;

    cart.forEach((item) => {
      const lineSubtotal = item.quantity * item.unitPrice;
      const gstRate = item.product.gstRate || 0;
      const itemGst = (lineSubtotal * gstRate) / 100;
      
      grossTotal += lineSubtotal;
      totalGst += itemGst;
    });

    const netAmount = Math.max(0, grossTotal + totalGst - totalDiscount);

    return {
      grossTotal,
      totalGst,
      totalDiscount,
      netAmount
    };
  };

  const { grossTotal, totalGst, totalDiscount, netAmount } = calculateTotals();

  const handleCheckout = async () => {
    if (cart.length === 0) {
      setErrorMessage('Cart is empty!');
      return;
    }
    if (!customerName.trim() || !/^\d{10}$/.test(customerPhone.trim())) {
      setErrorMessage('Enter a customer name and a valid 10-digit phone number.');
      return;
    }

    try {
      setProcessing(true);
      setErrorMessage('');
      const payload = {
        customerId: selectedCustomerId ? Number(selectedCustomerId) : null,
        customerName: customerName.trim(),
        customerPhone: customerPhone.trim(),
        paymentMethod: paymentMode,
        discountAmount: Number(discountAmount) || 0,
        paidAmount: paymentMode === 'CREDIT' ? 0 : netAmount,
        items: cart.map((item) => ({
          productId: item.product.id,
          quantity: item.quantity,
          unitPrice: item.unitPrice
        }))
      };

      const res = await api.post('/billing', payload);
      setCompletedSaleId(res.data.id);
      clearCart();
      fetchInitialData();
    } catch (err) {
      console.error(err);
      setErrorMessage(err.response?.data?.message || 'Failed to complete transaction!');
    } finally {
      setProcessing(false);
    }
  };

  const formatCurrency = (val) => {
    return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(val || 0);
  };

  const categoryProducts = productsList.filter((product) =>
    !selectedCategory || product.categoryName === selectedCategory
  );
  const browseProducts = categoryProducts.slice(0, 24);

  return (
    <div className="space-y-4">
      {/* Top Header Controls */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl p-4 border border-slate-100 dark:border-slate-700 shadow-sm">
        <div className="relative w-full">
          <Search className="w-5 h-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400" />
          <input
            ref={searchInputRef}
            type="text"
            placeholder="Search product by name or HSN code..."
            value={searchQuery}
            onChange={handleSearchChange}
            className="w-full pl-11 pr-4 py-2.5 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 text-slate-800 dark:text-slate-100 focus:outline-none focus:ring-2 focus:ring-emerald-500 text-sm"
          />

          {searchResults.length > 0 && (
            <div className="absolute left-0 right-0 top-full mt-2 bg-white dark:bg-slate-800 rounded-xl border border-slate-200 dark:border-slate-700 shadow-2xl z-50 max-h-64 overflow-y-auto divide-y divide-slate-100 dark:divide-slate-700">
              {searchResults.map((prod) => (
                <div
                  key={prod.id}
                  onClick={() => addToCart(prod)}
                  className="p-3 hover:bg-emerald-50 dark:hover:bg-slate-700/60 cursor-pointer flex justify-between items-center transition-colors"
                >
                  <div>
                    <h4 className="text-sm font-semibold text-slate-800 dark:text-slate-200">{prod.name}</h4>
                    <span className="text-xs text-slate-400">Stock: {prod.stockQuantity} {prod.unit}</span>
                  </div>
                  <span className="text-sm font-bold text-emerald-600">{formatCurrency(prod.sellingPrice)}</span>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {errorMessage && (
        <div className="bg-rose-50 dark:bg-rose-950/50 border border-rose-200 text-rose-700 dark:text-rose-300 px-4 py-3 rounded-xl text-sm font-medium animate-bounce">
          ⚠️ {errorMessage}
        </div>
      )}

      {/* Main Billing Layout */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        
        {/* Left Side: Cart Items Table (8 Cols) */}
        <div className="lg:col-span-8 space-y-4">
          
          {/* Quick Category Filters */}
          <div className="flex gap-2 overflow-x-auto pb-1 scrollbar-thin">
            <button
              onClick={() => setSelectedCategory('')}
              className={`px-3.5 py-1.5 rounded-full text-xs font-semibold whitespace-nowrap transition-colors ${
                selectedCategory === '' 
                  ? 'bg-emerald-600 text-white shadow' 
                  : 'bg-white dark:bg-slate-800 text-slate-600 dark:text-slate-300 border border-slate-200 dark:border-slate-700 hover:bg-slate-50'
              }`}
            >
              All Items
            </button>
            {categories.map((cat) => (
              <button
                key={cat.id}
                onClick={() => setSelectedCategory(cat.name)}
                className={`px-3.5 py-1.5 rounded-full text-xs font-semibold whitespace-nowrap transition-colors ${
                  selectedCategory === cat.name 
                    ? 'bg-emerald-600 text-white shadow' 
                    : 'bg-white dark:bg-slate-800 text-slate-600 dark:text-slate-300 border border-slate-200 dark:border-slate-700 hover:bg-slate-50'
                }`}
              >
                {cat.name}
              </button>
            ))}
          </div>

          <section className="bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm overflow-hidden">
            <div className="px-4 py-3 border-b border-slate-100 dark:border-slate-700 flex items-center justify-between">
              <h2 className="text-sm font-bold text-slate-800 dark:text-slate-100">Select a Product</h2>
              <span className="text-xs text-slate-400">{categoryProducts.length} items</span>
            </div>
            {browseProducts.length === 0 ? (
              <p className="p-4 text-sm text-slate-400">No products available in this category.</p>
            ) : (
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-2 p-3 max-h-72 overflow-y-auto">
                {browseProducts.map((product) => (
                  <button
                    key={product.id}
                    type="button"
                    disabled={product.stockQuantity <= 0}
                    onClick={() => addToCart(product)}
                    className="min-h-16 p-3 text-left border border-slate-200 dark:border-slate-700 rounded-lg hover:border-emerald-500 hover:bg-emerald-50 dark:hover:bg-slate-700 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-between gap-3"
                  >
                    <span className="min-w-0">
                      <span className="block text-sm font-semibold text-slate-800 dark:text-slate-100 truncate">{product.name}</span>
                      <span className="block text-xs text-slate-400">Stock: {product.stockQuantity} {product.unit}</span>
                    </span>
                    <span className="shrink-0 text-right">
                      <span className="block text-sm font-bold text-emerald-600">{formatCurrency(product.sellingPrice)}</span>
                      <Plus className="w-4 h-4 ml-auto text-emerald-600" />
                    </span>
                  </button>
                ))}
              </div>
            )}
            {categoryProducts.length > browseProducts.length && (
              <p className="px-4 pb-3 text-xs text-slate-400">Showing 24 items. Search above to find other products.</p>
            )}
          </section>

          {/* Cart Table Container */}
          <div className="bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm overflow-hidden flex flex-col min-h-[480px]">
            <div className="p-4 border-b border-slate-100 dark:border-slate-700 flex justify-between items-center bg-slate-50/50 dark:bg-slate-800">
              <h2 className="font-bold text-slate-800 dark:text-slate-100 flex items-center gap-2">
                <ShoppingBag className="w-5 h-5 text-emerald-600" />
                Current Cart Items ({cart.length})
              </h2>
              {cart.length > 0 && (
                <button
                  onClick={clearCart}
                  className="text-xs text-rose-600 hover:text-rose-700 font-semibold flex items-center gap-1"
                >
                  <Trash2 className="w-3.5 h-3.5" />
                  Clear Cart
                </button>
              )}
            </div>

            <div className="flex-1 overflow-x-auto">
              <table className="w-full text-left text-sm">
                <thead className="text-xs uppercase bg-slate-100/70 dark:bg-slate-700/50 text-slate-500 dark:text-slate-400">
                  <tr>
                    <th className="px-4 py-3">#</th>
                    <th className="px-4 py-3">Product Name</th>
                    <th className="px-4 py-3 text-right">Price</th>
                    <th className="px-4 py-3 text-center">Qty</th>
                    <th className="px-4 py-3 text-right">GST %</th>
                    <th className="px-4 py-3 text-right">Total</th>
                    <th className="px-4 py-3 text-center">Action</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 dark:divide-slate-700">
                  {cart.length === 0 ? (
                    <tr>
                      <td colSpan="7" className="py-24 text-center text-slate-400">
                        <div className="flex flex-col items-center gap-2">
                          <ShoppingBag className="w-12 h-12 stroke-[1.5] text-slate-300" />
                          <p className="font-medium text-slate-500">Cart is empty</p>
                          <p className="text-xs text-slate-400">Search for a product above and select it to start billing</p>
                        </div>
                      </td>
                    </tr>
                  ) : (
                    cart.map((item, idx) => {
                      const lineTotal = item.quantity * item.unitPrice * (1 + (item.product.gstRate || 0) / 100);
                      return (
                        <tr key={item.product.id} className="hover:bg-slate-50/50 dark:hover:bg-slate-700/30">
                          <td className="px-4 py-3 text-xs text-slate-400">{idx + 1}</td>
                          <td className="px-4 py-3 font-semibold text-slate-800 dark:text-slate-100">
                            {item.product.name}
                            <div className="text-xs font-normal text-slate-400">HSN: {item.product.hsnCode || 'N/A'}</div>
                          </td>
                          <td className="px-4 py-3 text-right font-medium text-slate-700 dark:text-slate-300">
                            {formatCurrency(item.unitPrice)}
                          </td>
                          <td className="px-4 py-3">
                            <div className="flex items-center justify-center gap-1.5">
                              <button
                                onClick={() => updateQuantity(item.product.id, -1)}
                                className="p-1 rounded bg-slate-100 dark:bg-slate-700 text-slate-600 dark:text-slate-300 hover:bg-slate-200"
                              >
                                <Minus className="w-3.5 h-3.5" />
                              </button>
                              <span className="w-8 text-center font-bold text-sm">{item.quantity}</span>
                              <button
                                onClick={() => updateQuantity(item.product.id, 1)}
                                className="p-1 rounded bg-slate-100 dark:bg-slate-700 text-slate-600 dark:text-slate-300 hover:bg-slate-200"
                              >
                                <Plus className="w-3.5 h-3.5" />
                              </button>
                            </div>
                          </td>
                          <td className="px-4 py-3 text-right text-xs text-slate-500">
                            {item.product.gstRate || 0}%
                          </td>
                          <td className="px-4 py-3 text-right font-bold text-emerald-600 dark:text-emerald-400">
                            {formatCurrency(lineTotal)}
                          </td>
                          <td className="px-4 py-3 text-center">
                            <button
                              onClick={() => removeFromCart(item.product.id)}
                              className="p-1 text-slate-400 hover:text-rose-600 transition-colors"
                            >
                              <Trash2 className="w-4 h-4" />
                            </button>
                          </td>
                        </tr>
                      );
                    })
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        {/* Right Side: Billing Summary & Payment (4 Cols) */}
        <div className="lg:col-span-4 space-y-4">
          
          {/* Checkout Card */}
          <div className="bg-white dark:bg-slate-800 rounded-2xl p-5 border border-slate-100 dark:border-slate-700 shadow-sm space-y-4">
            <h3 className="font-bold text-slate-800 dark:text-slate-100 text-base border-b border-slate-100 dark:border-slate-700 pb-3 flex items-center gap-2">
              <CreditCard className="w-5 h-5 text-emerald-600" />
              Bill Payment Summary
            </h3>

            <div className="space-y-2.5">
              <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider">Existing Customer</label>
              <select
                value={selectedCustomerId}
                onChange={(e) => {
                  const customerId = e.target.value;
                  const customer = customers.find((item) => String(item.id) === customerId);
                  setSelectedCustomerId(customerId);
                  setCustomerName(customer?.name || '');
                  setCustomerPhone(customer?.phone || '');
                }}
                className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 text-slate-800 dark:text-slate-100 text-sm"
              >
                <option value="">New customer</option>
                {customers.map((customer) => (
                  <option key={customer.id} value={customer.id}>{customer.name} ({customer.phone})</option>
                ))}
              </select>
              <div>
                <label className="block text-xs font-medium text-slate-500 mb-1">Customer Name</label>
                <input
                  type="text"
                  required
                  value={customerName}
                  onChange={(e) => {
                    setCustomerName(e.target.value);
                    setSelectedCustomerId('');
                  }}
                  placeholder="Enter customer name"
                  autoComplete="name"
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 text-slate-800 dark:text-slate-100 text-sm"
                />
              </div>
              <div>
                <label className="block text-xs font-medium text-slate-500 mb-1">Phone Number</label>
                <input
                  type="tel"
                  required
                  inputMode="numeric"
                  pattern="[0-9]{10}"
                  maxLength={10}
                  value={customerPhone}
                  onChange={(e) => {
                    setCustomerPhone(e.target.value.replace(/\D/g, ''));
                    setSelectedCustomerId('');
                  }}
                  placeholder="10-digit phone number"
                  autoComplete="tel-national"
                  className="w-full px-3 py-2 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 text-slate-800 dark:text-slate-100 text-sm"
                />
              </div>
            </div>

            <div className="space-y-2.5 text-sm">
              <div className="flex justify-between text-slate-600 dark:text-slate-400">
                <span>Gross Subtotal</span>
                <span className="font-semibold text-slate-800 dark:text-slate-200">{formatCurrency(grossTotal)}</span>
              </div>
              <div className="flex justify-between text-slate-600 dark:text-slate-400">
                <span>Total GST</span>
                <span className="font-semibold text-slate-800 dark:text-slate-200">{formatCurrency(totalGst)}</span>
              </div>
              
              <div className="flex justify-between items-center pt-1">
                <span className="text-slate-600 dark:text-slate-400 flex items-center gap-1">
                  <Tag className="w-3.5 h-3.5 text-emerald-600" />
                  Flat Discount (₹)
                </span>
                <input
                  type="number"
                  min="0"
                  value={discountAmount}
                  onChange={(e) => setDiscountAmount(e.target.value)}
                  className="w-24 px-2.5 py-1 text-right rounded-lg border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 font-semibold text-slate-800 dark:text-slate-100 text-sm focus:ring-1 focus:ring-emerald-500"
                />
              </div>

              <div className="border-t border-dashed border-slate-200 dark:border-slate-700 pt-3 flex justify-between items-center">
                <span className="font-bold text-base text-slate-800 dark:text-slate-100">Net Payable</span>
                <span className="font-extrabold text-xl text-emerald-600 dark:text-emerald-400">{formatCurrency(netAmount)}</span>
              </div>
            </div>

            {/* Payment Mode */}
            <div className="space-y-2 pt-2">
              <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider">Payment Method</label>
              <div className="grid grid-cols-2 gap-2">
                {['CASH', 'UPI', 'CARD', 'CREDIT'].map((mode) => (
                  <button
                    key={mode}
                    type="button"
                    onClick={() => setPaymentMode(mode)}
                    className={`py-2 rounded-xl text-xs font-bold border transition-all ${
                      paymentMode === mode
                        ? 'bg-emerald-600 text-white border-emerald-600 shadow'
                        : 'bg-slate-50 dark:bg-slate-700 text-slate-700 dark:text-slate-300 border-slate-200 dark:border-slate-600 hover:bg-slate-100'
                    }`}
                  >
                    {mode === 'CREDIT' ? 'UDHAR (CREDIT)' : mode}
                  </button>
                ))}
              </div>
            </div>

            {/* Cash Tendered Helper */}
            {paymentMode === 'CASH' && (
              <div className="space-y-2 pt-1 bg-emerald-50/50 dark:bg-emerald-950/30 p-3 rounded-xl border border-emerald-100 dark:border-emerald-900">
                <div className="flex justify-between items-center text-xs">
                  <span className="font-semibold text-slate-700 dark:text-slate-300">Cash Received (₹)</span>
                  <input
                    type="number"
                    placeholder="e.g. 500"
                    value={cashTendered}
                    onChange={(e) => setCashTendered(e.target.value)}
                    className="w-28 px-2 py-1 text-right rounded-lg border border-slate-300 dark:border-slate-600 font-bold text-sm bg-white dark:bg-slate-800"
                  />
                </div>
                {cashTendered && Number(cashTendered) >= netAmount && (
                  <div className="flex justify-between items-center text-xs font-bold text-emerald-700 dark:text-emerald-400 pt-1">
                    <span>Return Change:</span>
                    <span className="text-sm">{formatCurrency(Number(cashTendered) - netAmount)}</span>
                  </div>
                )}
              </div>
            )}

            {/* Submit Checkout Button */}
            <button
              onClick={handleCheckout}
              disabled={cart.length === 0 || processing}
              className="w-full py-3.5 bg-emerald-600 hover:bg-emerald-700 disabled:opacity-50 disabled:cursor-not-allowed text-white font-bold rounded-xl shadow-lg transition-all transform active:scale-95 flex items-center justify-center gap-2 text-base"
            >
              {processing ? (
                <>
                  <RefreshCw className="w-5 h-5 animate-spin" />
                  Generating Bill...
                </>
              ) : (
                <>
                  <CheckCircle2 className="w-5 h-5" />
                  Complete & Print Bill (F8)
                </>
              )}
            </button>
          </div>

        </div>

      </div>

      {/* Invoice Modal after completed sale */}
      {completedSaleId && (
        <InvoiceModal
          saleId={completedSaleId}
          onClose={() => setCompletedSaleId(null)}
        />
      )}
    </div>
  );
};

export default POSPage;
