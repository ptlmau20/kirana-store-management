import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../api/api';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem('kirana_user');
    return savedUser ? JSON.parse(savedUser) : null;
  });

  const [token, setToken] = useState(() => localStorage.getItem('kirana_token'));
  const [loading, setLoading] = useState(false);

  const login = async (username, password) => {
    setLoading(true);
    try {
      const response = await api.post('/auth/login', { username, password });
      const data = response.data;
      
      const userData = {
        username: data.username,
        role: data.role,
        fullName: data.fullName,
        mustChangePassword: data.mustChangePassword
      };

      setToken(data.token);
      setUser(userData);

      localStorage.setItem('kirana_token', data.token);
      localStorage.setItem('kirana_user', JSON.stringify(userData));

      return { success: true, user: userData };
    } catch (error) {
      const message = error.response?.data?.message || 'Invalid username or password';
      return { success: false, error: message };
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('kirana_token');
    localStorage.removeItem('kirana_user');
  };

  const roleStr = user?.role ? user.role.replace('ROLE_', '') : '';
  const isAdmin = roleStr === 'ADMIN';
  const isCashier = roleStr === 'CASHIER';

  return (
    <AuthContext.Provider value={{ user, token, isAuthenticated: !!token, isAdmin, isCashier, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
