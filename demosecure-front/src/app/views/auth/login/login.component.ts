import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  UntypedFormGroup,
  UntypedFormBuilder,
  Validators,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { AlertController, IonicModule } from '@ionic/angular';
import { TokenService } from 'src/app/services/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, ReactiveFormsModule ],
})
export class LoginComponent implements OnInit {
  loginForm?: UntypedFormGroup;
  submitted = false;
  hideFormLogin: boolean;

  constructor(
    public alertCtrl: AlertController,
    private formBuilder: UntypedFormBuilder,
    private tokenService: TokenService
  ) {
    //Par defaut on masque le formulaire de login car on va tenter de recuperer les
    //identifiants de connexion avant dans le coffre fort du tel
    this.hideFormLogin = true;
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      email: ['jon.doe@gmail.com', Validators.required],
      password: ['johndoe', Validators.required]
    });
  }

  onSubmit() {
    // stop here if form is invalid
    if (!this.loginForm || this.loginForm.invalid) {
      return;
    }
    //recupere les champs
    let email = this.loginForm.get('email')?.value;
    let pass = this.loginForm.get('password')?.value;
    //Effecture la connexion avec les identifiants
    this.proceedLogin(email, pass);
  }

  /**
   * Effectue la connexion avec les identifiants donnes
   * @param email : le login
   * @param pass : le mot de passe
   */
  private proceedLogin(email: string, pass: string) {
    this.submitted = true; //soumission en cours
    //Appel du service pour la connexion
    this.tokenService.login(email, pass).subscribe({
      next: (rToken) => console.log('token recupere : %s', rToken),
      error: (e) => console.error(e),
      complete: () => {
        console.info('complete');
        this.submitted = false;
      },
    });
  }
}
