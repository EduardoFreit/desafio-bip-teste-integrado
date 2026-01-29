import { Component, EventEmitter, inject, Output, signal, TemplateRef, ViewChild, WritableSignal } from '@angular/core';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { BeneficioDTO } from '../../../api';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-editar-modal',
  imports: [FormsModule],
  templateUrl: './editar-modal.html',
  styleUrl: './editar-modal.css',
})
export class EditarModal {

  private modalService = inject(NgbModal);
	closeResult: WritableSignal<string> = signal('');
  @ViewChild('content') contentTemplate!: TemplateRef<any>;

  @Output() editado = new EventEmitter<BeneficioDTO>();

  beneficioEdicao : WritableSignal<BeneficioDTO> = signal<BeneficioDTO>(this.setarBeneficioEdicao());

  private setarBeneficioEdicao(beneficio ?: BeneficioDTO): BeneficioDTO {
    return beneficio ? { ...beneficio } : { nome: '', descricao: '', valor: 0.00, ativo: true };
  }

	open(beneficio: BeneficioDTO) {
    this.beneficioEdicao.set(this.setarBeneficioEdicao(beneficio));
		this.modalService.open(this.contentTemplate, { ariaLabelledBy: 'modal-basic-title' });
	}

  editar() {
    this.editado.emit(this.beneficioEdicao());
  }

}
