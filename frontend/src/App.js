import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link, Navigate } from 'react-router-dom';
import './styles/App.css';
import logo from './assets/images/sakila-logo.png';
import Home from './components/Home';
import Movies from './components/Movies';
import Movie from './components/Movie';
import Rental from './components/Rental';
import Stores from './components/Stores';
import Login from './components/Login';

const App = () => {
    return (
        <Router>
           <div className="app-container">
                <header className="header">
                    <img className="header-logo" src={logo} alt="logo" />
                    <nav className="nav">
                        <Link to="/home">Home</Link>
                        <Link to="/movies">Movies</Link>
                        <Link to="/rental">Rental</Link>
                        <Link to="/stores">Stores</Link>
                    </nav>
                    <Link to="/login" className="signIn">Sign in</Link>
                </header>

                <div className="content">
                    <Routes>
                        <Route path="/" element={<Navigate to="/home" />} />
                        <Route path="/home" element={<Home />} />
                        <Route path="/movies" element={<Movies />} />
                        <Route path="/movies/:id" element={<Movie />} />
                        <Route path="/rental" element={<Rental />} />
                        <Route path="/stores" element={<Stores />} />
                        <Route path="/login" element={<Login />} />
                    </Routes>
                    
                </div>

                <footer className="footer">
                    <p>© 2024 Sakila Movies</p>
                </footer>
            </div>
        </Router>
    );
};

export default App;