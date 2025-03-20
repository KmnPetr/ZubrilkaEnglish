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
import {jsonEditorReducer} from "./reducers/jsonEditorReducer"
import { voiceEditorReducer } from "./reducers/voiceEditorReducer";
import {videoInfoReducer} from "./reducers/videoInfoReducer.js";
import {cardEditorReducer} from "./reducers/cardEditorReducer.js";





const rootReducer = combineReducers({
    videoManagementReducer: videoManagementReducer,
    videoReducer: videoReducer,
    networkReducer: networkReducer,
    authReducer: authReducer,
    phraseReducer: phraseReducer,
    translationReducer: translationReducer,
    jsonEditorReducer: jsonEditorReducer,
    voiceEditorReducer: voiceEditorReducer,
    cardEditorReducer: cardEditorReducer,
    videoInfoReducer: videoInfoReducer
})

export const store = createStore(rootReducer,composeWithDevTools(applyMiddleware(thunk,phrasesMiddleware)))