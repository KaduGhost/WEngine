package br.com.redewalker.engine.sistema.reports.eventos;

import br.com.redewalker.engine.sistema.reports.Report;
import br.com.redewalker.engine.sistema.reports.ReportPerfil;
import br.com.redewalker.engine.WEngine;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReportCreateEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private Report report;
	
	public ReportCreateEvent(Report report) {
		this.report = report;
	}
	
	public Report getReport() {
		return report;
	}
	
	public ReportPerfil getReportPerfil() {
		return WEngine.get().getReportManager().getReportPerfil(report.getReportado());
	}
	
	public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
