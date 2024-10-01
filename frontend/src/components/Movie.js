import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import '../styles/Movies.css';
import movieService from '../services/movieService';

const Movie = () => {
    const { id } = useParams();
    const [movie, setMovie] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setLoading(true);
        movieService.getMovieById(id)
            .then(data => {
                setMovie(data)
                setLoading(false)
            })
            .catch(err => {
                setError(err)
                setLoading(false)
            });
    }, [id]);

    if (loading) {
        return <p>Loading...</p>;
    }

    return (
        <div className="movies-container">
            <main>
                {error && <p>Error fetching movie: {error.message}</p>}
                {movie ? (
                    <div>
                        <div className='page-title'>{movie.title}, {movie.releaseYear}</div>
                        <p>{movie.description}</p>
                    </div>
                        
                ) : (
                    <p>No movie found.</p>
                )}
            </main>
        </div>
    );
};

export default Movie;