import { useEffect, useState } from 'react';
import client from '../api/client';

export default function OrdersPage() {
  const [orders, setOrders] = useState([]);

  const load = async () => {
    const res = await client.get('/orders');
    setOrders(res.data);
  };

  useEffect(() => { load(); }, []);

  const cancel = async (orderId) => {
    await client.post(`/orders/${orderId}/cancel`);
    load();
  };

  return (
    <div>
      <h2>Your Orders</h2>
      {orders.map((o) => (
        <div key={o.id} className="order">
          <h4>Order #{o.id} - {o.status}</h4>
          <p>Total: ${o.totalPrice}</p>
          <p>Shipping: {o.shippingAddress}</p>
          {o.status !== 'CANCELLED' && <button onClick={() => cancel(o.id)}>Cancel</button>}
          <ul>
            {o.orderItems.map((i) => (
              <li key={`${o.id}-${i.productId}`}>{i.productName} x {i.quantity} = ${i.lineTotal}</li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
}

