import React from "react";
import * as ReactDOMClient from "react-dom/client"
import "./css/main.css"
import "./css/NotFoundPage.css";
import "./components/workPlace/listPhrases/listPhrases.css"
import {Provider} from "react-redux";
import {store} from "./redux/initRedux";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import ListVideo from "./pages/ListVideo";
import EditVideo from "./pages/EditVideo";
import HomePage from "./pages/HomePage";


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