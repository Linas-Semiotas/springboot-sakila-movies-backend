import axios from 'axios';

const API_URL = 'http://localhost:8080/api/rental';

const getAllRentals = () => {
    return axios.get(`${API_URL}`)
        .then(response => response.data)
        .catch(error => {
            console.error('Error fetching movies:', error.response ? error.response.data : error.message);
            throw error;
        });
};

const getRentalById = (id) => {
    return axios.get(`${API_URL}/${id}`)
        .then(response => response.data)
        .catch(error => {
            console.error(`Error fetching movie with ID ${id}:`, error.response ? error.response.data : error.message);
            throw error;
        });
};

const rentalService = {
    getAllRentals,
    getRentalById
};

export default rentalService;