import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Pagination } from '@mui/material';
import '../styles/Movies.css';
import movieService from '../services/movieService';

const Movies = () => {
    const [movies, setMovies] = useState([]);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 20;

    useEffect(() => {
        movieService.getAllMovies()
            .then(data => setMovies(data))
            .catch(err => setError(err));
    }, []);

    const totalPages = Math.ceil(movies.length / itemsPerPage);
    const indexOfLastMovie = currentPage * itemsPerPage;
    const indexOfFirstMovie = indexOfLastMovie - itemsPerPage;
    const currentMovies = movies.slice(indexOfFirstMovie, indexOfLastMovie);

    const handlePageChange = (event, value) => {
        setCurrentPage(value);
    };

    return (
        <div>
            <div className='page-title'>Sakila movie list</div>
            <div className="movies-container gallery">
                {error && <p>Error fetching movies: {error.message}</p>}
                {currentMovies.map(movie => (
                    <Link to={`/movies/${movie.id}`} key={movie.id} className="item">
                        <div className="item-content">
                            <h2>{movie.title}</h2>
                            <h4>{movie.releaseYear}</h4>
                        </div>
                    </Link>
                ))}
            </div>
            <div className="pagination">
                <Pagination 
                    count={totalPages} 
                    page={currentPage} 
                    onChange={handlePageChange}
                    variant="outlined"
                    color='primary'
                    size="large"
                />
            </div>
        </div>
    );
};

export default Movies;