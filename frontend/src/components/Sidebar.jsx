import React from 'react';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import {
  LayoutDashboard,
  ShoppingCart,
  Package,
  Layers,
  Users,
  FileText,
  UserCog,
  ShieldAlert,
  Key
} from 'lucide-react';

const Sidebar = () => {
  const { isAdmin } = useAuth();

  const navItems = [
    { label: 'Dashboard', path: '/', icon: LayoutDashboard },
    { label: 'POS Billing', path: '/pos', icon: ShoppingCart, badge: 'F2' },
    { label: 'Add Product & Stock', path: '/products', icon: Package },
    { label: 'Stock Movements', path: '/inventory', icon: Layers },
    { label: 'Customers & Udhar', path: '/customers', icon: Users },
    { label: 'Sales History', path: '/sales-history', icon: FileText },
  ];

  const adminItems = [
    { label: 'User Management', path: '/admin/users', icon: UserCog },
    { label: 'Security Audit Logs', path: '/admin/audit-logs', icon: ShieldAlert },
  ];

  return (
    <aside className="w-64 bg-slate-900 border-r border-slate-800 flex flex-col justify-between py-4 px-3 select-none">
      <div className="space-y-6">
        <div>
          <p className="px-3 text-[11px] font-semibold text-slate-500 uppercase tracking-wider mb-2">
            Main Operations
          </p>
          <nav className="space-y-1">
            {navItems.map((item) => {
              const Icon = item.icon;
              return (
                <NavLink
                  key={item.path}
                  to={item.path}
                  className={({ isActive }) =>
                    `flex items-center justify-between px-3 py-2.5 rounded-xl text-xs font-medium transition ${
                      isActive
                        ? 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/20 shadow-sm'
                        : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/60'
                    }`
                  }
                >
                  <div className="flex items-center space-x-3">
                    <Icon className="w-4 h-4" />
                    <span>{item.label}</span>
                  </div>
                  {item.badge && (
                    <span className="text-[10px] bg-slate-800 text-slate-400 font-mono px-1.5 py-0.5 rounded border border-slate-700">
                      {item.badge}
                    </span>
                  )}
                </NavLink>
              );
            })}
          </nav>
        </div>

        {isAdmin && (
          <div>
            <p className="px-3 text-[11px] font-semibold text-amber-500/80 uppercase tracking-wider mb-2 flex items-center gap-1.5">
              <span>Admin Controls</span>
            </p>
            <nav className="space-y-1">
              {adminItems.map((item) => {
                const Icon = item.icon;
                return (
                  <NavLink
                    key={item.path}
                    to={item.path}
                    className={({ isActive }) =>
                      `flex items-center space-x-3 px-3 py-2.5 rounded-xl text-xs font-medium transition ${
                        isActive
                          ? 'bg-amber-500/15 text-amber-400 border border-amber-500/20'
                          : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/60'
                      }`
                    }
                  >
                    <Icon className="w-4 h-4 text-amber-400/80" />
                    <span>{item.label}</span>
                  </NavLink>
                );
              })}
            </nav>
          </div>
        )}
      </div>

      <div className="pt-4 border-t border-slate-800/80 space-y-1">
        <NavLink
          to="/profile"
          className={({ isActive }) =>
            `flex items-center space-x-3 px-3 py-2.5 rounded-xl text-xs font-medium transition ${
              isActive
                ? 'bg-slate-800 text-white'
                : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/50'
            }`
          }
        >
          <Key className="w-4 h-4" />
          <span>Change Password</span>
        </NavLink>
      </div>
    </aside>
  );
};

export default Sidebar;
