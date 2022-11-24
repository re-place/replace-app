import { Injectable } from '@angular/core';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  constructor(private router: Router) { }

  isLoggedIn: boolean = false;


  login(user: string, pass: string) {
      // send login request
      this.isLoggedIn = true; 
      this.router.navigate(['overview']);
  }
}
