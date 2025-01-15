import {applyMiddleware, combineReducers, createStore} from "redux";
import {composeWithDevTools} from "@redux-devtools/extension";
import {thunk} from "redux-thunk";
import {phraseReducer} from "./reducers/phraseReduser";
import {videoManagementReducer} from "./reducers/videoManagementReducer";
import {videoReducer} from "./reducers/videoReducer";
import {networkReducer} from "./reducers/networkReducer";
import {authReducer} from "./reducers/authReducer";
import {translationReducer} from "./reducers/translationReducer";
import {phrasesMiddleware} from "./middlewares"





const rootReducer = combineReducers({
    videoManagementReducer: videoManagementReducer,
    videoReducer: videoReducer,
    networkReducer: networkReducer,
    authReducer: authReducer,
    phraseReducer: phraseReducer,
    translationReducer: translationReducer
})

export const store = createStore(rootReducer,composeWithDevTools(applyMiddleware(thunk,phrasesMiddleware)))