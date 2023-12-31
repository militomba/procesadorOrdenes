import dayjs from 'dayjs';

export interface IOrden {
  id?: number;
  operacion?: string | null;
  fechaOperacion?: string | null;
  modo?: string | null;
  precio?: number | null;
  accionId?: number | null;
  cantidad?: number | null;
  cliente?: number | null;
  accion?: string | null;
  operacionExitosa?: boolean | null;
  operacionObservaciones?: string | null;
  reportada?: boolean | null;
}

export const defaultValue: Readonly<IOrden> = {
  operacionExitosa: false,
  reportada: false,
};
