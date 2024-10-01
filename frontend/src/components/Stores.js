import React, { useState, useEffect } from 'react';
import '../styles/Stores.css';
import storeService from '../services/storeService';

const Stores = () => {
    const [stores, setStores] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        storeService.getAllStores()
            .then(data => setStores(data))
            .catch(err => setError(err));
    }, []);

    return (
        <div>
            <div className='page-title'>Our stores</div>
            <div className="stores-container">
                {error && <p>Error fetching stores: {error.message}</p>}
                {stores.map(store => (
                    <div key={store.storeId}>
                        <h2>{store.country} address</h2>
                        <p>{store.country}, {store.city}, {store.address}</p>
                        <br /><hr />
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Stores;
