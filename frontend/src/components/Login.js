import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';
import '../styles/Login.css';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/login', {
                username,
                password
            });
            if (response.status === 200) {
                navigate('/home');
            }
        } catch (error) {
            setError('Invalid username or password');
        }
    };

    return (
        <div className="login-container">
            <div className='page-title'>Login</div>
            <div className="login-wrapper">
                <form onSubmit={handleSubmit}>
                    <div className="input-group">
                        <input
                            placeholder='Username'
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    <div className="input-group">
                        <input
                            placeholder='Password'
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button className="login-button" type="submit">Login</button>
                    {error && <p className="error-message">{error}</p>}
                    <p className="register-link">
                        Don't have an account?&nbsp;
                        <Link to={`/register`}>Create an account</Link>
                    </p>
                </form>
            </div>
        </div>
    );
};

export default Login;
