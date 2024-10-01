import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/Register.css';
import storeService from '../services/storeService';

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
        try {
            const response = await axios.post('http://localhost:8080/register', {
                firstName,
                lastName,
                email,
                storeId: selectedStore,
                username,
                password
            });
            if (response.status === 200) {
                navigate('/home');
            }
        } catch (error) {
            setError('Error during registration');
        }
    };

    useEffect(() => {
        storeService.getAllStores()
            .then(data => setStores(data))
            .catch(err => setError(err));
    }, []);

    return (
        <div className="register-container">
            <div className='page-title'>Register</div>
            <div className="register-wrapper">
                <form onSubmit={handleSubmit}>
                <div className="input-group">
                        <input
                            placeholder='First name'
                            type="text"
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                            required
                        />
                        <input
                            placeholder='Last name'
                            type="text"
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
                            required
                        />
                    </div>
                    <div className="input-single">
                        <input
                            placeholder='Email'
                            type="text"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div className="input-group">
                        <label>Store location</label>
                        <select id="store"
                            value={selectedStore}
                            onChange={(e) => {setSelectedStore(e.target.value);}}
                            required
                        >
                            <option value="">Select a store</option>
                            {stores.map((store, index) => (
                                <option key={index} value={store.storeId}>{store.country}</option>
                            ))}
                        </select>
                    </div>
                    <div className="input-single">
                        <input
                            placeholder='Username'
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    <div className="input-single">
                        <input
                            placeholder='Password'
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button className="register-button" type="submit">Register</button>
                    {error && <p className="error-message">{error}</p>}
                </form>
            </div>
        </div>
    );
};

export default Register;
