import { useState, useEffect } from 'react';
import { Box, Tabs, Tab } from '@mui/material';
import { Routes, Route, useLocation, useNavigate, Navigate } from 'react-router-dom';
import '../styles/User.css';
import Orders from './UserOrders';
import Balance from './UserBalance';
import Profile from './UserProfile';

const User = () => {
    const [value, setValue] = useState(0);
    const navigate = useNavigate();
    const location = useLocation();

    const tabTitles = ['Users', 'Movies', 'Stores'];

    useEffect(() => {
        if (location.pathname === '/admin/users') {
            setValue(0);
        } else if (location.pathname === '/admin/movies') {
            setValue(1);
        }
        else if (location.pathname === '/admin/stores') {
            setValue(2);
        }
    }, [location]);

    const handleChange = (event, newValue) => {
        setValue(newValue);
        if (newValue === 0) {
            navigate('/admin/users');
        } else if (newValue === 1) {
            navigate('/admin/movies');
        }
        else if (newValue === 2) {
            navigate('/admin/stores');
        }
    };

    return (
        <div>
            <div className='page-title'>{tabTitles[value]}</div>
            <div className="user-main-container">
                <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                    <Tabs value={value} onChange={handleChange} aria-label="user tabs" 
                        sx={{
                            '& .MuiTabs-indicator': {
                                height: '3px',
                                color: '#333',
                                backgroundColor: '#333',
                                borderRadius: '2px',
                            },
                            '& .MuiTab-root': {
                                textTransform: 'none',
                                minWidth: 'auto',
                                padding: '0.5rem 1rem',
                                fontSize: '1rem',
                                fontWeight: 600,
                                color: '#333',
                                '&:hover': {
                                    backgroundColor: '#f4f4f4',
                                    color: '#222'
                                },
                                '&.Mui-selected': {
                                    backgroundColor: '#f4f4f4',
                                    color: '#222'
                                },
                            },
                        }}
                    >
                        <Tab label="Users" />
                        <Tab label="Movies" />
                        <Tab label="Stores" />
                    </Tabs>
                </Box>
                <Routes>
                    <Route path="/" element={<Navigate to="users" />} />
                    <Route path="users" element={<Orders />} />
                    <Route path="movies" element={<Balance />} />
                    <Route path="stores" element={<Profile />} />
                </Routes>
            </div>
        </div>
    );
};

export default User;
