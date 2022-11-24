import { Component } from '@angular/core';
import {AppService} from 'src/app/services/app.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
    user: string = '';
    pass: string = '';

    constructor(private appService: AppService) {}

    login() {
        this.appService.login(this.user, this.pass);
    }
}
