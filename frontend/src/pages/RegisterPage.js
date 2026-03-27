import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import client from '../api/client';
import { useAuth } from '../context/AuthContext';

export default function RegisterPage() {
  const [form, setForm] = useState({ name: '', email: '', password: '' });
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const res = await client.post('/auth/register', form);
      login(res.data);
      navigate('/');
    } catch (err) {
      setError(err?.response?.data?.message || 'Register failed');
    }
  };

  return (
    <div className="auth-card">
      <h2>Register</h2>
      <form onSubmit={submit}>
        <input placeholder="Name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
        <input placeholder="Email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
        <input type="password" placeholder="Password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
        {error && <p className="error">{error}</p>}
        <button type="submit">Create Account</button>
      </form>
      <p>Already registered? <Link to="/login">Login</Link></p>
    </div>
  );
}

