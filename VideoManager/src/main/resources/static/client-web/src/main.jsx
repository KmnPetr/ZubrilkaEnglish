import React from "react";
import * as ReactDOMClient from "react-dom/client"
import "./css/main.css"
import "./pages/notFoundPage/NotFoundPage.css";
import "./pages/editVideo/workPlace/listPhrases/listPhrases.css"
import {Provider} from "react-redux";
import {store} from "./redux/initRedux";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import LoginPage from "./pages/loginPage/LoginPage.jsx";
import ListVideo from "./pages/listVideo/ListVideo.jsx";
import EditVideo from "./pages/editVideo/EditVideo.jsx";
import HomePage from "./pages/homePage/HomePage.jsx";


const router = createBrowserRouter([
    {
        path: "/",
        element: <HomePage/>
    },
    {
        path: "/login",
        element: <LoginPage/>
    },
    {
        path: "/listVideo",
        element: <ListVideo/>
    },
    {
        path: "/editVideo/:videoInfoUuid",
        element: <EditVideo/>
    }
]);

const app = ReactDOMClient.createRoot(document.getElementById("root"))
app.render(
    <Provider store={store}>
        <RouterProvider router={router} />
    </Provider>
)