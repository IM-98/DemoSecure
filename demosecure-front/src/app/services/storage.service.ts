import { Injectable } from '@angular/core';
import { Storage } from '@ionic/storage-angular';
import {environment} from "../../environments/environment";
@Injectable({
  providedIn: 'root'
})
export class StorageService {
  private _storage: Storage | null = null;
  private session_token: string = "";
  constructor(private storage: Storage) {
    this.init();
  }
  async init() {
    const storage = await this.storage.create();
    this._storage = storage;
  }
  public set(key: string, value: any) {
    this.session_token = value;
    this._storage?.set( environment.tokenKey, value);
  }
  public get(key: string): Promise<any> | undefined {
    return this._storage?.get(environment.tokenKey);
  }
  /**
   * Renvoie le token actuel
   * @returns le token actuel
   */
  public getToken(): string | undefined {
    return this.session_token;
  }
}

