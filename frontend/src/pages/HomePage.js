import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import client from '../api/client';
import { useAuth } from '../context/AuthContext';

export default function HomePage() {
  const [products, setProducts] = useState([]);
  const [search, setSearch] = useState('');
  const [maxPrice, setMaxPrice] = useState('');
  const [message, setMessage] = useState('');
  const { isAuthenticated } = useAuth();

  const load = async () => {
    const res = await client.get('/products');
    setProducts(res.data);
  };

  useEffect(() => {
    load();
  }, []);

  const addToCart = async (id) => {
    try {
      await client.post('/cart/items', { productId: id, quantity: 1 });
      setMessage('Added to cart');
      setTimeout(() => setMessage(''), 1000);
    } catch {
      setMessage('Login required for cart');
    }
  };

  const filtered = products.filter((p) => {
    const matchSearch = p.name.toLowerCase().includes(search.toLowerCase());
    const matchPrice = maxPrice ? Number(p.price) <= Number(maxPrice) : true;
    return matchSearch && matchPrice;
  });

  return (
    <div>
      <div className="filters">
        <input placeholder="Search products" value={search} onChange={(e) => setSearch(e.target.value)} />
        <input placeholder="Max price" value={maxPrice} onChange={(e) => setMaxPrice(e.target.value)} />
      </div>
      {message && <p>{message}</p>}
      <div className="grid">
        {filtered.map((p) => (
          <div key={p.id} className="card">
            <img src={p.imageUrl || 'https://via.placeholder.com/200'} alt={p.name} />
            <h3>{p.name}</h3>
            <p>{p.description}</p>
            <p className="price">${p.price}</p>
            <div className="actions">
              <Link to={`/products/${p.id}`}>View</Link>
              {isAuthenticated && <button onClick={() => addToCart(p.id)}>Add to cart</button>}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

