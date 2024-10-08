import React, { useState, useEffect } from 'react';
import { getPersonalInfo, updatePersonalInfo } from '../services/userService';
import '../styles/User.css';

const UserProfile = () => {
    const [personalInfoError, setPersonalInfoError] = useState(null);
    //const [addressError, setAddressError] = useState(null);
    const [personalInfoSuccess, setPersonalInfoSuccess] = useState(null);
    //const [addressSuccess, setAddressSuccess] = useState(null);
    const [personalInfo, setPersonalInfo] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phone: ''
    });

    useEffect(() => {
        getPersonalInfo()
            .then(response => {
                setPersonalInfo(response);
                setPersonalInfoError(null);
            })
            .catch(error => {
                console.error("Error fetching personal information:", error);
                setPersonalInfoError("There was an error loading your personal information. Please try again.");
            });
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setPersonalInfo(prevInfo => ({ ...prevInfo, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        updatePersonalInfo(personalInfo)
            .then(response => {
                setPersonalInfoSuccess('Personal information updated successfully');
                setPersonalInfoError(null); // Reset error after a successful update
            })
            .catch(error => {
                setPersonalInfoError("There was an error updating your personal information. Please try again.");
                setPersonalInfoSuccess(null); // Reset success if there's an error
            });
    };

    return (
        <div>
            <div className="user-container">
                <h2>Personal Info</h2>
                <form onSubmit={handleSubmit}>
                    <div className="input-group">
                        <label>First Name:</label>
                        <div className="input-wrapper">
                            <input 
                                placeholder='First name'
                                type="text" 
                                name="firstName" 
                                value={personalInfo.firstName || ''} 
                                onChange={handleChange} 
                            />
                        </div>
                    </div>
                    <div className="input-group">
                        <label>Last Name:</label>
                        <div className="input-wrapper">
                            <input 
                                placeholder='Last name'
                                type="text" 
                                name="lastName" 
                                value={personalInfo.lastName || ''} 
                                onChange={handleChange} 
                            />
                        </div>
                    </div>
                    <div className="input-group">
                        <label>Email:</label>
                        <div className="input-wrapper">
                            <input 
                                placeholder='Email'
                                type="email" 
                                name="email" 
                                value={personalInfo.email || ''} 
                                onChange={handleChange} 
                            />
                        </div>
                    </div>
                    <div className="input-group">
                        <label>Phone:</label>
                        <div className="input-wrapper">
                            <input 
                                placeholder='Phone number'
                                type="tel" 
                                name="phone" 
                                value={personalInfo.phone || ''} 
                                onChange={handleChange} 
                            />
                        </div>
                    </div>
                    <button className="submit-button" type="submit">Save Changes</button>
                </form>
                {personalInfoError && <p className="error-message">{personalInfoError}</p>}
                {personalInfoSuccess && <p className="success-message">{personalInfoSuccess}</p>}
            </div>
            <div className="user-container">
                <h2>Address</h2>
                placeholder for address
                {/* {addressError && <p className="error-message">{addressError}</p>} */}
                {/* {addressSuccess && <p className="success-message">{addressSuccess}</p>} */}
            </div>
        </div>
    );
};

export default UserProfile;
