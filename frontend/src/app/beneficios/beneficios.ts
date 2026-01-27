import { Component, ViewChildren, ViewChild, QueryList, ChangeDetectorRef, ChangeDetectionStrategy } from '@angular/core';
import { AtualizarRequestParams, BeneficioDTO, CriarRequestParams, DeletarRequestParams, GerenciamentoDeBenefciosService, ListarRequestParams, PageBeneficioDTO, TransferenciaRequest, TransferirRequestParams } from '../api';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap/pagination';
import { CurrencyPipe, CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbdSortableHeader, SortEvent } from './directive/sortable.directive';
import { NgbTooltip } from '@ng-bootstrap/ng-bootstrap/tooltip';
import { CriarModal } from './components/criar-modal/criar-modal';
import { EditarModal } from './components/editar-modal/editar-modal';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { TransferirModal } from './components/transferir-modal/transferir-modal';

@Component({
  selector: 'app-beneficios',
  imports: [NgbPagination, CurrencyPipe, FormsModule, NgbdSortableHeader, CommonModule, NgbTooltip, CriarModal, EditarModal, TransferirModal],
  templateUrl: './beneficios.html',
  styleUrl: './beneficios.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class Beneficios {

  @ViewChildren(NgbdSortableHeader) headers!: QueryList<NgbdSortableHeader>;
  
  @ViewChild(CriarModal) criarModal!: CriarModal;
  @ViewChild(EditarModal) editarModal!: EditarModal;
  @ViewChild(TransferirModal) transferirModal!: TransferirModal;

  constructor(
    private beneficioApi: GerenciamentoDeBenefciosService, 
    private cdr: ChangeDetectorRef,
    private modalService: NgbModal
  ) {
  }

  pageBeneficio: PageBeneficioDTO = {};

  sizeOptions: number[] = [5, 10, 25];
  sizeSelected: number = 5;
  collectionSize: number = 0;
  page: number = 1;
  pageSize: number = 5;
  sortColumn: string = 'nome';
  sortDirection: string = 'asc';

  ngOnInit() {
    this.recarregarBeneficios();
  }

  recarregarBeneficios() {
    let filtroBeneficio : ListarRequestParams = {
      page: this.page - 1,
      size: this.pageSize,
      sort: [`${this.sortColumn},${this.sortDirection}`]
    };
    this.beneficioApi.listar(filtroBeneficio).subscribe((resp: PageBeneficioDTO) => {
      this.pageBeneficio = {};
      this.pageBeneficio = { ...resp, content: [...resp.content!] };
      this.collectionSize = resp.totalElements || 0;
      this.page = (resp.number || 0) + 1;
      this.pageSize = resp.size || 5;
      this.cdr.detectChanges();
    });
  }

  trackById(_index: number, beneficio: BeneficioDTO) {
    return beneficio.id;
  }

  onSort({ column, direction }: SortEvent) {
    this.headers.forEach((header: NgbdSortableHeader) => {
      if (header.sortable !== column) {
        header.direction = '';
      }
    });
    this.sortColumn = column;
    this.sortDirection = direction;
    this.recarregarBeneficios();
  }

  deletarBeneficio(id: number) {
    let deletarRequestParams : DeletarRequestParams = { id };
    this.beneficioApi.deletar(deletarRequestParams).subscribe(() => {
      this.recarregarBeneficios();
    });
  }

  abrirModalCriarBeneficio() {
    this.criarModal.open();
  }

  criarBeneficio (beneficio: BeneficioDTO) {
    let criarRequestParams : CriarRequestParams = { beneficioDTO: beneficio };
    console.log(criarRequestParams);
    this.beneficioApi.criar(criarRequestParams).subscribe(() => {
      this.recarregarBeneficios();
      this.modalService.dismissAll();
    });
  }

  abrirModalEditarBeneficio(beneficio: BeneficioDTO) {
    this.editarModal.open(beneficio);
  }

  editarBeneficio (beneficio: BeneficioDTO) {
    let criarRequestParams : AtualizarRequestParams = { beneficioDTO: beneficio, id: beneficio.id! };
    this.beneficioApi.atualizar(criarRequestParams).subscribe(() => {
      this.recarregarBeneficios();
      this.modalService.dismissAll();
    });
  }

  abrirModalTransferirBeneficio(beneficio: BeneficioDTO) {
    let transferirRequest: TransferenciaRequest = { contaDestinoId: 0, contaOrigemId: beneficio.id!, valor: 0.00 };
    let transferirRequestParams: TransferirRequestParams = { transferenciaRequest: transferirRequest };
    this.transferirModal.open(transferirRequestParams);
  }

  transferirBeneficio (transferirRequestParams: TransferirRequestParams) {
    console.log(transferirRequestParams);
    this.beneficioApi.transferir(transferirRequestParams).subscribe(() => {
      this.recarregarBeneficios();
      this.modalService.dismissAll();
    });
  }
}