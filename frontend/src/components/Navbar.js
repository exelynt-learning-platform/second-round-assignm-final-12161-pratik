import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const onLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="brand">ShopSphere</div>
      <div className="nav-links">
        <Link to="/">Home</Link>
        {isAuthenticated && <Link to="/cart">Cart</Link>}
        {isAuthenticated && <Link to="/orders">Orders</Link>}
        {!isAuthenticated && <Link to="/login">Login</Link>}
        {!isAuthenticated && <Link to="/register">Register</Link>}
        {isAuthenticated && <button onClick={onLogout}>Logout</button>}
      </div>
    </nav>
  );
}

