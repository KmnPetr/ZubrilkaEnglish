import React from 'react';
import {Link, useNavigate} from 'react-router-dom';


const NotFoundPage = () => {
    const navigate = useNavigate();

    const handleGoBack = () => {
        navigate('/'); // Возврат на главную страницу
    };

    return (
        <div className="not-found-container">
            <div className="not-found-content">
                <h1 className="not-found-title">404</h1>
                <p className="not-found-text">Oops! The page you're looking for doesn't exist.</p>
                <button className="go-back-btn" onClick={handleGoBack}>Go Back Home</button>
            </div>
            <Link to="/">Home from Link</Link>
            <div className="not-found-icon">
                {/*<img*/}
                {/*    src="https://via.placeholder.com/150"*/}
                {/*    alt="Not Found Icon"*/}
                {/*/>*/}
            </div>
        </div>
    );
};

export default NotFoundPage;