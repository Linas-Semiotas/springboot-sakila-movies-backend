import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Register.css';
import storeService from '../services/storeService';
import { register } from '../services/authService';

const Register = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [stores, setStores] = useState([]);
    const [selectedStore, setSelectedStore] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(''); // Clear error before submitting
        try {
            const response = await register(username, password, firstName, lastName, email, selectedStore);
            if (response.status === 200) {
                navigate('/home'); // Navigate to home on successful registration
            }
        } catch (error) {
            if (error.response && error.response.data) {
                setError(error.response.data.message || 'Error during registration');
            } else {
                setError('Error during registration');
            }
        }
    };

    useEffect(() => {
        storeService.getAllStores()
            .then(data => setStores(data))
            .catch(err => setError(err));
    }, []);

    return (
        <div className="register-container">
            <div className="page-title">Register</div>
            <div className="register-wrapper">
                <form onSubmit={handleSubmit}>
                    <div className="input-group">
                        <input
                            placeholder="First name"
                            type="text"
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                            required
                        />
                        <input
                            placeholder="Last name"
                            type="text"
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
                            required
                        />
                    </div>
                    <div className="input-single">
                        <input
                            placeholder="Email"
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div className="input-group">
                        <label>Store location</label>
                        <select
                            value={selectedStore}
                            onChange={(e) => setSelectedStore(e.target.value)}
                            required
                        >
                            <option value="">Select a store</option>
                            {stores.map((store) => (
                                <option key={store.storeId} value={store.storeId}>{store.country}</option>
                            ))}
                        </select>
                    </div>
                    <div className="input-single">
                        <input
                            placeholder="Username"
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    <div className="input-single">
                        <input
                            placeholder="Password"
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button className="register-button" type="submit">Register</button>
                    {error && <p className="error-message">{error}</p>} {/* Render the error message */}
                </form>
            </div>
        </div>
    );
};

export default Register;
