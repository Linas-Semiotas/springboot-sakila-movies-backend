import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { login } from '../services/authService';
import '../styles/Login.css';

const Login = ({onLoginSuccess}) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');
        try {
          const data = await login(username, password);
          localStorage.setItem('token', data.token);
          onLoginSuccess();
          navigate('/home');
        } catch (error) {
          if (error.response && error.response.status === 401) {
            setError('Invalid username or password');
          } else {
            setError('An error occurred. Please try again.');
          }
        }
      };

    return (
        <div className="login-container">
            <div className='page-title'>Login</div>
            <div className="login-wrapper">
                <form onSubmit={handleLogin}>
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
