import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { logout } from '../services/authService';

const Logout = () => {
    const navigate = useNavigate();

    useEffect(() => {
        let isMounted = true;

        const performLogout = async () => {
            await logout();
            if (isMounted) {
                navigate('/login');
            }
        };

        performLogout();

        return () => {
            isMounted = false;
        };
    }, [navigate]);

    return null;
};

export default Logout;
