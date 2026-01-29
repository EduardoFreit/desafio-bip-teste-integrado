import { Component, ViewChildren, ViewChild, QueryList, signal } from '@angular/core';
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
import { ToastrService } from 'ngx-toastr';
import { RemoverModal } from './components/remover-modal/remover-modal';

@Component({
  selector: 'app-beneficios',
  imports: [NgbPagination, CurrencyPipe, FormsModule, NgbdSortableHeader, CommonModule, NgbTooltip, CriarModal, EditarModal, TransferirModal, RemoverModal],
  templateUrl: './beneficios.html',
  styleUrl: './beneficios.css'
})
export class Beneficios {

  @ViewChildren(NgbdSortableHeader) headers!: QueryList<NgbdSortableHeader>;
  
  @ViewChild(CriarModal) criarModal!: CriarModal;
  @ViewChild(EditarModal) editarModal!: EditarModal;
  @ViewChild(TransferirModal) transferirModal!: TransferirModal;
  @ViewChild(RemoverModal) removerModal!: RemoverModal;

  constructor(
    private beneficioApi: GerenciamentoDeBenefciosService, 
    private modalService: NgbModal,
    private toastr: ToastrService
  ) {
  }
  
  // Filtros, tabela e paginação
  sizeOptions: number[] = [5, 10, 25];
  beneficios = signal<Array<BeneficioDTO>>([]);
  collectionSize = signal<number>(0);
  page = signal<number>(1);
  pageSize = signal<number>(5);
  filtroNome = signal<string>('');
  
  // Ordenação
  sortColumn = signal<string>('nome');
  sortDirection = signal<string>('asc')

  ngOnInit() {
    this.recarregarBeneficios();
  }

  recarregarBeneficios() {
    let filtroBeneficio : ListarRequestParams = {
      page: this.page() - 1,
      size: this.pageSize(),
      sort: [`${this.sortColumn()},${this.sortDirection()}`],
      nome: this.filtroNome()
    };
    this.beneficioApi.listar(filtroBeneficio).subscribe({
      next: (resp: PageBeneficioDTO) => {
        this.beneficios.set(resp.content || []);
        this.collectionSize.set(resp.totalElements || 0);
        this.page.set((resp.number || 0) + 1);
        this.pageSize.set(resp.size || 5);
      },
      error: (error) => {
        this.toastr.error(error.error.message, 'Erro');
      }
    });
  }

  onSort({ column, direction }: SortEvent) {
    this.headers.forEach((header: NgbdSortableHeader) => {
      if (header.sortable !== column) {
        header.direction = '';
      }
    });
    this.sortColumn.set(column);
    this.sortDirection.set(direction);
    this.recarregarBeneficios();
  }

  abrirModalRemoverBeneficio(beneficio: BeneficioDTO) {
    this.removerModal.open(beneficio);
  }

  deletarBeneficio(id: number) {
    let deletarRequestParams : DeletarRequestParams = { id };
    this.beneficioApi.deletar(deletarRequestParams).subscribe({
      next: () => {
        this.recarregarBeneficios();
        this.modalService.dismissAll();
        this.toastr.success('Benefício deletado com sucesso!', 'Sucesso');
      },
      error: (error) => {
        this.toastr.error(error.error.message, 'Erro');
      }
    });
  }

  abrirModalCriarBeneficio() {
    this.criarModal.open();
  }

  criarBeneficio (beneficio: BeneficioDTO) {
    let criarRequestParams : CriarRequestParams = { beneficioDTO: beneficio };
    this.beneficioApi.criar(criarRequestParams).subscribe({
      next: () => {
        this.recarregarBeneficios();
        this.modalService.dismissAll();
        this.toastr.success('Benefício criado com sucesso!', 'Sucesso');
      },
      error: (error) => {
        this.toastr.error(error.error.message, 'Erro');
      }
    });
  }

  abrirModalEditarBeneficio(beneficio: BeneficioDTO) {
    this.editarModal.open(beneficio);
  }

  editarBeneficio (beneficio: BeneficioDTO) {
    let criarRequestParams : AtualizarRequestParams = { beneficioDTO: beneficio, id: beneficio.id! };
    this.beneficioApi.atualizar(criarRequestParams).subscribe({
      next: () => {
        this.recarregarBeneficios();
        this.modalService.dismissAll();
        this.toastr.success('Benefício editado com sucesso!', 'Sucesso');
      },
      error: (error) => {
        this.toastr.error(error.error.message, 'Erro');
      }
    });
  }

  abrirModalTransferirBeneficio(beneficio: BeneficioDTO) {
    let transferirRequest: TransferenciaRequest = { contaDestinoId: 0, contaOrigemId: beneficio.id!, valor: 0.00 };
    let transferirRequestParams: TransferirRequestParams = { transferenciaRequest: transferirRequest };
    this.transferirModal.open(transferirRequestParams);
  }

  transferirBeneficio (transferirRequestParams: TransferirRequestParams) {
    this.beneficioApi.transferir(transferirRequestParams).subscribe({
      next: () => {
        this.recarregarBeneficios();
        this.toastr.success('Saldo transferido com sucesso!', 'Sucesso');
      },
      error: (error) => {
        this.toastr.error(error.error.message, 'Erro');
      }
    });
  }

  limparFiltro() {
    this.filtroNome.set('');
    this.recarregarBeneficios();
  }
}