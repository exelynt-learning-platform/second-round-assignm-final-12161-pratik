import { useEffect, useState } from 'react';
import client from '../api/client';
import { useNavigate } from 'react-router-dom';

export default function CartPage() {
  const [cart, setCart] = useState({ items: [], total: 0 });
  const [shippingAddress, setShippingAddress] = useState('');
  const [msg, setMsg] = useState('');
  const navigate = useNavigate();

  const load = async () => {
    const res = await client.get('/cart');
    setCart(res.data);
  };
  useEffect(() => { load(); }, []);

  const updateQty = async (productId, quantity) => {
    await client.put(`/cart/items/${productId}`, { quantity: Number(quantity) });
    load();
  };

  const remove = async (productId) => {
    await client.delete(`/cart/items/${productId}`);
    load();
  };

  const placeOrder = async () => {
    if (!shippingAddress) return;
    await client.post('/orders', { shippingAddress });
    setMsg('Order placed successfully');
    setShippingAddress('');
    load();
    navigate('/orders');
  };

  return (
    <div>
      <h2>Your Cart</h2>
      {msg && <p>{msg}</p>}
      {cart.items?.map((item) => (
        <div className="row" key={item.productId}>
          <span>{item.productName}</span>
          <input type="number" min="1" value={item.quantity}
                 onChange={(e) => updateQty(item.productId, e.target.value)} />
          <span>${item.lineTotal}</span>
          <button onClick={() => remove(item.productId)}>Remove</button>
        </div>
      ))}
      <h3>Total: ${cart.total}</h3>
      <input placeholder="Shipping address" value={shippingAddress} onChange={(e) => setShippingAddress(e.target.value)} />
      <button onClick={placeOrder}>Place Order</button>
    </div>
  );
}

