import React from 'react';
import {useNavigate} from "react-router-dom";

const HomePage = () => {
    
    const navigate = useNavigate();

    const toLoginPage = () => {
        navigate("/login");
    }

    return (
        <div>
            <h1>Home Page</h1>
            <br/>
            
            {/* Добавляем onClick для обработки перехода */}
            <a href="#" onClick={(e) => { 
                e.preventDefault();
                toLoginPage(); 
            }}>
                To Login Page
            </a>
        </div>
    );
};

export default HomePage;