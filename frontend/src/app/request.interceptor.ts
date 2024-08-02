import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const requestInterceptor: HttpInterceptorFn = (req, next) => {
  if (localStorage !== undefined){
    const token = localStorage.getItem('token');

    if (token){
      // Clone the request and add the authorization header
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
  }

  // Pass the cloned request with the updated header to the next handler
  return next(req).pipe(
    catchError((err: any) => {
      if (err instanceof HttpErrorResponse) {
        // Handle HTTP errors
        const restError = err.error as RestError; // Cast to your custom error type
        const errorMessage = `Error Code: ${restError.status}\nError: ${restError.error}\nMessage: ${restError.message}\nPath: ${restError.path}`;
        console.log(errorMessage);

        if (restError.error === 'Invalid credentials')
            alert("Uneseni kredencijali nisu validni!");
        else if (restError.error === 'Username already exists')
            alert("Korisničko ime je već zauzeto!");
        else if (restError.error === 'Invalid account')
            alert("Vaš nalog nije validan!");
        else if (restError.error === 'Malicious request')
          alert("Detektovan je potencijalno maliciozan zahtjev!");
        else
            alert("Niste autorizovani za pristup ovom resursu!");
          // You might trigger a re-authentication flow or redirect the user here
        if (err.status === 401) {
          // Specific handling for unauthorized errors         
          //console.error('Unauthorized request:', err);
        } else {
          // Handle other HTTP error codes
        }
      } else {
        // Handle non-HTTP errors
        //console.error('An error occurred:', err);
      }

      // Re-throw the error to propagate it further
      return throwError(() => err); 
    })
  );;  
};

interface RestError {
  status: number;
  error: string;
  message: string;
  path: string;
}