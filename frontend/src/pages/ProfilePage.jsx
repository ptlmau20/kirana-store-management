import React, { useState } from 'react';
import api from '../api/api';
import { useAuth } from '../context/AuthContext';
import { KeyRound, CheckCircle, AlertCircle, ShieldCheck } from 'lucide-react';

const ProfilePage = () => {
  const { user } = useAuth();

  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const [successMsg, setSuccessMsg] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const [loading, setLoading] = useState(false);

  const handlePasswordChange = async (e) => {
    e.preventDefault();
    setErrorMsg('');
    setSuccessMsg('');

    if (newPassword !== confirmPassword) {
      setErrorMsg('New password and confirm password do not match!');
      return;
    }

    if (newPassword.length < 8) {
      setErrorMsg('Password must be at least 8 characters long!');
      return;
    }

    const hasUpper = /[A-Z]/.test(newPassword);
    const hasLower = /[a-z]/.test(newPassword);
    const hasDigit = /[0-9]/.test(newPassword);

    if (!hasUpper || !hasLower || !hasDigit) {
      setErrorMsg('Password must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number!');
      return;
    }

    try {
      setLoading(true);
      await api.post('/users/change-password', {
        currentPassword,
        newPassword,
        confirmPassword
      });
      setSuccessMsg('Your password has been changed successfully! BCrypt hash updated.');
      setCurrentPassword('');
      setNewPassword('');
      setConfirmPassword('');
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Failed to change password. Please check your current password.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div className="bg-white dark:bg-slate-800 rounded-2xl p-6 border border-slate-100 dark:border-slate-700 shadow-sm">
        <h1 className="text-2xl font-bold text-slate-800 dark:text-slate-100 flex items-center gap-2">
          <ShieldCheck className="w-7 h-7 text-emerald-600" />
          Account Security & Profile Settings
        </h1>
        <p className="text-slate-500 dark:text-slate-400 text-sm mt-1">Manage your store account credentials and password security</p>
      </div>

      {/* User Info Card */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl p-6 border border-slate-100 dark:border-slate-700 shadow-sm space-y-3">
        <div className="flex items-center justify-between border-b border-slate-100 dark:border-slate-700 pb-3">
          <span className="text-xs font-bold text-slate-400 uppercase">Logged in Account</span>
          <span className="px-2.5 py-1 rounded-full text-xs font-bold bg-emerald-100 text-emerald-800">{user?.role}</span>
        </div>
        <div className="grid grid-cols-2 gap-4 text-sm">
          <div>
            <span className="text-xs text-slate-400 block">Username</span>
            <span className="font-bold text-slate-800 dark:text-slate-100 font-mono">{user?.username}</span>
          </div>
          <div>
            <span className="text-xs text-slate-400 block">Full Name</span>
            <span className="font-bold text-slate-800 dark:text-slate-100">{user?.fullName || user?.username}</span>
          </div>
        </div>
      </div>

      {/* Password Change Form */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl p-6 border border-slate-100 dark:border-slate-700 shadow-sm space-y-4">
        <h2 className="font-bold text-slate-800 dark:text-slate-100 text-base flex items-center gap-2">
          <KeyRound className="w-5 h-5 text-emerald-600" />
          Change Password
        </h2>

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

        <form onSubmit={handlePasswordChange} className="space-y-4 text-sm">
          <div>
            <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Current Password</label>
            <input
              type="password"
              required
              value={currentPassword}
              onChange={(e) => setCurrentPassword(e.target.value)}
              className="w-full px-4 py-2.5 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 font-mono"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-500 uppercase mb-1">New Password</label>
            <input
              type="password"
              required
              placeholder="Min 8 chars, 1 uppercase, 1 lowercase, 1 digit"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              className="w-full px-4 py-2.5 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 font-mono"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-500 uppercase mb-1">Confirm New Password</label>
            <input
              type="password"
              required
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              className="w-full px-4 py-2.5 rounded-xl border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 font-mono"
            />
          </div>

          <div className="pt-2">
            <button
              type="submit"
              disabled={loading}
              className="w-full py-3.5 bg-emerald-600 hover:bg-emerald-700 text-white font-bold rounded-xl shadow-lg transition-all"
            >
              {loading ? 'Updating Password Hash...' : 'Update Password'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ProfilePage;
