import React, { createContext, useContext, useMemo, useState } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [role, setRole] = useState(localStorage.getItem('role'));

  const login = (authResponse) => {
    localStorage.setItem('token', authResponse.token);
    localStorage.setItem('role', authResponse.role);
    setToken(authResponse.token);
    setRole(authResponse.role);
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    setToken(null);
    setRole(null);
  };

  const value = useMemo(() => ({ token, role, login, logout, isAuthenticated: !!token }), [token, role]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}

