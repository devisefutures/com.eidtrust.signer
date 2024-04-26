// TODO ASSINA Update this to point to the deployed address of the assina-server
//      See https://create-react-app.dev/docs/adding-custom-environment-variables/
export const ASSINA_RSSP_BASE_URL = process.env.REACT_APP_ASSINA_RSSP_BASE_URL || 'http://localhost:8080';
export const ASSINA_SA_BASE_URL = process.env.REACT_APP_ASSINA_SA_BASE_URL || 'http://localhost:8081';
export const ASSINA_CLIENT_BASE_URL = process.env.REACT_APP_ASSINA_CLIENT_BASE_URL || 'http://localhost:3000';

export const API_BASE_URL = ASSINA_RSSP_BASE_URL + '/api/v1';
export const CSC_BASE_URL = ASSINA_RSSP_BASE_URL + '/csc/v1';

// TODO ASSINA Update this to point to the deployed address of the assina-client react server
export const OAUTH2_REDIRECT_URI = ASSINA_CLIENT_BASE_URL + '/oauth2/redirect'

export const GOOGLE_AUTH_URL = ASSINA_RSSP_BASE_URL + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const FACEBOOK_AUTH_URL = ASSINA_RSSP_BASE_URL + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const GITHUB_AUTH_URL = API_BASE_URL + '/oauth2/authorize/github?redirect_uri=' + OAUTH2_REDIRECT_URI;

console.log('API_BASE_URL:        ' + API_BASE_URL);
console.log('CSC_BASE_URL:        ' + CSC_BASE_URL);
console.log('ASSINA_SA_BASE_URL:  ' + ASSINA_SA_BASE_URL);
console.log('OAUTH2_REDIRECT_URI: ' + OAUTH2_REDIRECT_URI);

export const ACCESS_TOKEN = 'accessToken';
