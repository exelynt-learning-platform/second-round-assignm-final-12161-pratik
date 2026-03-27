import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import client from '../api/client';

export default function ProductDetailsPage() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);

  useEffect(() => {
    client.get(`/products/${id}`).then((res) => setProduct(res.data));
  }, [id]);

  if (!product) return <p>Loading...</p>;
  return (
    <div className="details">
      <img src={product.imageUrl || 'https://via.placeholder.com/300'} alt={product.name} />
      <div>
        <h2>{product.name}</h2>
        <p>{product.description}</p>
        <p className="price">${product.price}</p>
        <p>Stock: {product.stockQuantity}</p>
      </div>
    </div>
  );
}

