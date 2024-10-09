import React, { useState, useEffect } from 'react';
import { fetchLanguages, addLanguage, deleteLanguage } from '../../services/adminService';

const AdminMovies = () => {
    const [languages, setLanguages] = useState([]);
    const [newLanguage, setNewLanguage] = useState('');
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    useEffect(() => {
        fetchLanguages()
            .then(setLanguages)
            .catch(() => setError("Error fetching languages."));
    }, []);

    const handleAddLanguage = () => {
        setError(null);
        setSuccess(null);
        addLanguage(newLanguage)
            .then(() => {
                setSuccess("Language added successfully.");
                setNewLanguage(''); // Reset the input field
                fetchLanguages().then(setLanguages);
            })
            .catch(err => setError(err.response?.data?.error || "Error adding language."));
    };

    const handleDeleteLanguage = async (languageId) => {
        try {
            await deleteLanguage(languageId);
            setSuccess('Language deleted successfully.');
            setError(null);
            fetchLanguages();
        } catch (error) {
            const errorMessage = error.response?.data?.error || 'Error deleting language. It might be in use.';
            setError(errorMessage);
        }
    };

    return (
        <div className="admin-container">
            <h2>Languages</h2>
            <div className="admin-container-row">
                <div className="admin-table-container admin-table-container-language">
                    <table className="admin-table">
                        <thead className="admin-table-header">
                            <tr>
                                <th style={{ width: '70%', textAlign: 'left' }}>Language</th>
                                <th style={{ width: '30%' }}>Action</th>
                            </tr>
                        </thead>
                    </table>
                    <div className="admin-table-body admin-table-body-language">
                        <table className="admin-table">
                            <tbody>
                                {languages.map(lang => (
                                    <tr key={lang.id}>
                                        <td style={{ width: '70%', textAlign: 'left' }}>{lang.name}</td>
                                        <td style={{ width: '30%' }}>
                                            <button className="delete-button" onClick={() => handleDeleteLanguage(lang.id)}>Delete</button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
                <div className="admin-details admin-details-language">
                    <h3>Add New Language</h3>
                    <div className="input-wrapper">
                        <input
                            type="text"
                            placeholder="New Language"
                            value={newLanguage}
                            onChange={(e) => setNewLanguage(e.target.value)}
                            className="input-field"
                        />
                        <button  className="add-button" onClick={handleAddLanguage}>Add</button>
                    </div>
                </div>
            </div>
            {error && <p className="error-message">{error}</p>}
            {success && <p className="success-message">{success}</p>}
        </div>
    );
};

export default AdminMovies;
