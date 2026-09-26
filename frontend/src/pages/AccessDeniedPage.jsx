import React from 'react';
import { Link } from 'react-router-dom';
import { ShieldAlert, ArrowLeft } from 'lucide-react';

const AccessDeniedPage = () => {
  return (
    <div className="flex flex-col items-center justify-center min-h-[70vh] text-center px-4">
      <div className="w-20 h-20 bg-rose-100 dark:bg-rose-950/60 text-rose-600 rounded-3xl flex items-center justify-center mb-6 shadow-inner">
        <ShieldAlert className="w-10 h-10" />
      </div>
      <h1 className="text-3xl font-extrabold text-slate-800 dark:text-slate-100">403 - Access Denied</h1>
      <p className="text-slate-500 dark:text-slate-400 mt-2 max-w-md text-sm">
        You do not have administrative privileges to access this restricted management feature. Cashiers are restricted to POS billing, sales, customers, and payments.
      </p>
      <Link
        to="/pos"
        className="mt-6 px-6 py-3 bg-emerald-600 hover:bg-emerald-700 text-white font-semibold rounded-xl shadow-lg transition-all flex items-center gap-2"
      >
        <ArrowLeft className="w-4 h-4" />
        Return to POS Billing Screen
      </Link>
    </div>
  );
};

export default AccessDeniedPage;
