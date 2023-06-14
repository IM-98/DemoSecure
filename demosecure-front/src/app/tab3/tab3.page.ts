import {Component, OnInit} from '@angular/core';
import {IonicModule} from '@ionic/angular';
import {ExploreContainerComponent} from '../explore-container/explore-container.component';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {RoleEnum} from "../model/RoleEnum";
import {CommonModule} from "@angular/common";
import {UserServiceService} from "../services/user-service.service";

@Component({
  selector: 'app-tab3',
  templateUrl: 'tab3.page.html',
  styleUrls: ['tab3.page.scss'],
  standalone: true,
  imports: [IonicModule, ExploreContainerComponent, ReactiveFormsModule, CommonModule],
})
export class Tab3Page implements OnInit{
  constructor(
    private formBuilder: FormBuilder,
    private userService: UserServiceService,
  ) {}
  roleForm?: FormGroup



  role = RoleEnum;

  ngOnInit() {
    this.roleForm = this.formBuilder.group({
      username: ['', Validators.required],
      role: ['', Validators.required]
    });
    }

    onSubmit() {

      const idRole: number = this.roleForm?.get('role')?.value.valueOf()
      const username: string = this.roleForm?.get('username')?.value
      this.userService.updateRole(username, idRole).subscribe({
        next:() => console.log("role updated"),
        error:(e) => console.log(e),
        complete: () => console.log("complete")
        })
    }

}
