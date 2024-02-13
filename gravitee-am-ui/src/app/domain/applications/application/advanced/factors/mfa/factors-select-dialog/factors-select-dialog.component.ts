/*
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { MfaFactor } from '../../model';
import { MfaIconsResolver } from '../mfa-icons-resolver';

export interface DialogData {
  factors: MfaFactor[];
  domainName: string;
  environment: string;
}

export interface DialogResult {
  changed: boolean;
  factors: MfaFactor[];
}

@Component({
  selector: 'app-factors-select-dialog',
  templateUrl: './factors-select-dialog.component.html',
  styleUrls: ['./factors-select-dialog.component.scss'],
})
export class FactorsSelectDialogComponent {
  iconResolver = new MfaIconsResolver();
  factors: MfaFactor[];
  model: any;

  constructor(public dialogRef: MatDialogRef<FactorsSelectDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogData) {
    this.factors = data.factors;
    this.model = data.factors.reduce((map, obj) => {
      map[obj.id] = obj.selected;
      return map;
    }, {});
  }

  confirmSelection() {
    this.factors.forEach((factor) => (factor.selected = this.model[factor.id]));
    const result = {
      factors: this.factors,
      changed: true,
    } as DialogResult;
    this.dialogRef.close(result);
  }

  cancel() {
    this.dialogRef.close();
  }

  goToMfaFactorSettingsPage(event: any) {
    event.preventDefault();
    const url = `/environments/${this.data.environment}/domains/${this.data.domainName}/settings/factors`;
    window.open(url, '_blank');
  }
}
