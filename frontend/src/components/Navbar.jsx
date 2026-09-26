import React from 'react';
import { useAuth } from '../context/AuthContext';
import { ShoppingBag, LogOut, ShieldCheck, UserCheck } from 'lucide-react';

const Navbar = () => {
  const { user, isAdmin, logout } = useAuth();
  const displayName = user?.username === 'admin' ? 'Mihir Patel' : user?.fullName || user?.username;

  return (
    <header className="h-16 bg-slate-900 border-b border-slate-800 px-6 flex items-center justify-between sticky top-0 z-30 shadow-md">
      <div className="flex items-center space-x-3">
        <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-emerald-600 to-teal-400 flex items-center justify-center shadow-lg shadow-emerald-500/20">
          <ShoppingBag className="w-5 h-5 text-white" />
        </div>
        <div>
          <h1 className="font-bold text-lg text-white tracking-wide flex items-center gap-2">
            MAHAKALI KIRANA STORE
            <span className="text-xs bg-emerald-500/20 text-emerald-400 px-2 py-0.5 rounded-md font-medium border border-emerald-500/30">
              POS & Stock System
            </span>
          </h1>
        </div>
      </div>

      <div className="flex items-center space-x-4">
        <div className="flex items-center space-x-3 px-3 py-1.5 rounded-xl bg-slate-800/80 border border-slate-700/60">
          <div className="w-8 h-8 rounded-lg bg-slate-700 flex items-center justify-center text-slate-200">
            {isAdmin ? <ShieldCheck className="w-4 h-4 text-amber-400" /> : <UserCheck className="w-4 h-4 text-emerald-400" />}
          </div>
          <div className="text-left hidden sm:block">
            <p className="text-xs font-semibold text-slate-100">{displayName}</p>
            <p className="text-[10px] text-slate-400 uppercase font-medium tracking-wider">
              {user?.role === 'ROLE_ADMIN' ? 'Administrator' : 'Cashier'}
            </p>
          </div>
        </div>

        <button
          onClick={logout}
          className="flex items-center space-x-1.5 bg-rose-500/10 hover:bg-rose-500/20 text-rose-400 border border-rose-500/20 px-3.5 py-1.5 rounded-xl transition text-xs font-semibold"
          title="Sign out of system"
        >
          <LogOut className="w-4 h-4" />
          <span className="hidden sm:inline">Logout</span>
        </button>
      </div>
    </header>
  );
};

export default Navbar;
