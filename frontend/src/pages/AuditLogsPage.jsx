import React, { useState, useEffect } from 'react';
import api from '../api/api';
import { 
  FileText, 
  RefreshCw, 
  ShieldAlert, 
  CheckCircle, 
  XCircle,
  Clock
} from 'lucide-react';

const AuditLogsPage = () => {
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchAuditLogs();
  }, []);

  const fetchAuditLogs = async () => {
    try {
      setLoading(true);
      const res = await api.get('/reports/audit-logs');
      setLogs(res.data || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Banner */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl p-6 border border-slate-100 dark:border-slate-700 shadow-sm flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800 dark:text-slate-100 flex items-center gap-2">
            <FileText className="w-7 h-7 text-emerald-600" />
            Security & Authentication Audit Trail
          </h1>
          <p className="text-slate-500 dark:text-slate-400 text-sm mt-1">Immutable security log monitoring login attempts, failed logins, account locks & password changes</p>
        </div>

        <button
          onClick={fetchAuditLogs}
          className="bg-slate-100 dark:bg-slate-700 hover:bg-slate-200 text-slate-700 dark:text-slate-200 font-semibold px-4 py-2.5 rounded-xl shadow transition-colors flex items-center gap-2 text-sm"
        >
          <RefreshCw className="w-4 h-4" />
          Refresh Security Log
        </button>
      </div>

      {/* Log Table */}
      <div className="bg-white dark:bg-slate-800 rounded-2xl border border-slate-100 dark:border-slate-700 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead className="text-xs uppercase bg-slate-50 dark:bg-slate-700/50 text-slate-500 dark:text-slate-400">
              <tr>
                <th className="px-4 py-3.5">Timestamp</th>
                <th className="px-4 py-3.5">Username</th>
                <th className="px-4 py-3.5">Security Event</th>
                <th className="px-4 py-3.5 text-center">Status</th>
                <th className="px-4 py-3.5">IP Address</th>
                <th className="px-4 py-3.5">User Agent Details</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100 dark:divide-slate-700 font-mono text-xs">
              {loading ? (
                <tr>
                  <td colSpan="6" className="py-12 text-center text-slate-400 font-sans">Loading security audit logs...</td>
                </tr>
              ) : logs.length === 0 ? (
                <tr>
                  <td colSpan="6" className="py-12 text-center text-slate-400 font-sans">No security audit events recorded.</td>
                </tr>
              ) : (
                logs.map((log) => (
                  <tr key={log.id} className="hover:bg-slate-50/50 dark:hover:bg-slate-700/30">
                    <td className="px-4 py-3 text-slate-400">
                      {new Date(log.createdAt).toLocaleString('en-IN')}
                    </td>
                    <td className="px-4 py-3 font-bold text-slate-800 dark:text-slate-200">
                      {log.username}
                    </td>
                    <td className="px-4 py-3">
                      <span className={`px-2.5 py-1 rounded-full text-[11px] font-bold ${
                        log.eventType === 'LOGIN_SUCCESS' ? 'bg-emerald-100 text-emerald-800' :
                        log.eventType === 'LOGIN_FAILED' ? 'bg-rose-100 text-rose-800' :
                        log.eventType === 'ACCOUNT_LOCKED' ? 'bg-amber-100 text-amber-800' :
                        'bg-purple-100 text-purple-800'
                      }`}>
                        {log.eventType}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-center">
                      {log.success ? (
                        <span className="text-emerald-600 flex items-center justify-center gap-1 font-bold">
                          <CheckCircle className="w-3.5 h-3.5" /> OK
                        </span>
                      ) : (
                        <span className="text-rose-600 flex items-center justify-center gap-1 font-bold">
                          <XCircle className="w-3.5 h-3.5" /> FAIL
                        </span>
                      )}
                    </td>
                    <td className="px-4 py-3 text-slate-600 dark:text-slate-400">
                      {log.ipAddress || '127.0.0.1'}
                    </td>
                    <td className="px-4 py-3 text-slate-400 truncate max-w-xs" title={log.userAgent}>
                      {log.userAgent || 'Mozilla/5.0'}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default AuditLogsPage;
