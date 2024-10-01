import axios from 'axios';

const API_URL = 'http://localhost:8080/api/movies';

// const getAllMovies = async () => {
//     const token = localStorage.getItem('authToken');
//     console.log('Token:', token);
//     try {
//         const response = await axios.get(API_URL, {
//             headers: {
//                 'Authorization': `Bearer ${token}`
//             },
//             withCredentials: true
//         });
//         return response.data;
//     } catch (error) {
//         console.error('Error fetching movies:', error.response ? error.response.data : error.message);
//         throw error;
//     }
// };

// const getMovieById = async (id) => {
//     const token = localStorage.getItem('authToken');

//     try {
//         const response = await axios.get(`${API_URL}/${id}`, {
//             headers: {
//                 'Authorization': `Bearer ${token}`
//             },
//             withCredentials: true
//         });
//         return response.data;
//     } catch (error) {
//         console.error(`Error fetching movie with ID ${id}:`, error.response ? error.response.data : error.message);
//         throw error;
//     }
// };

const getAllMovies = () => {
    return axios.get(`${API_URL}`)
        .then(response => response.data)
        .catch(error => {
            console.error('Error fetching movies:', error.response ? error.response.data : error.message);
            throw error;
        });
};

const getMovieById = (id) => {
    return axios.get(`${API_URL}/${id}`)
        .then(response => response.data)
        .catch(error => {
            console.error(`Error fetching movie with ID ${id}:`, error.response ? error.response.data : error.message);
            throw error;
        });
};

const movieService = {
    getAllMovies,
    getMovieById
};

export default movieService;