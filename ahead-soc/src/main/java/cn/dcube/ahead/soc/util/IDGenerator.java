package cn.dcube.ahead.soc.util;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class IDGenerator {

	private AtomicLong m_eventCount = new AtomicLong(0);

	private Long m_uiDateCurrent;

	public IDGenerator() {
		m_uiDateCurrent = System.currentTimeMillis() / 1000;
	}

	public Long GetEvtBaseId(Long nodeId) {
		Long uiDateTmp = System.currentTimeMillis() / 1000;
		if (uiDateTmp != m_uiDateCurrent) {
			m_uiDateCurrent = uiDateTmp;
		}
		Long uiCurrTime = m_uiDateCurrent << 32;
		return (uiCurrTime + nodeId + (m_eventCount.getAndIncrement() & 0x00FFFFFF));
	}

}
