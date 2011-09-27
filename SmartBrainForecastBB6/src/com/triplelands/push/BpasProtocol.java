/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.triplelands.push;


/**
 * Network protocol for BPAS server. Its implementation depends on handheld firmware and will use the latest API available.
 */
public interface BpasProtocol {

    /**
     * Registers with BPAS server
     */
    public void register() throws Exception;

    /**
     * Unregisters from BPAS server
     */
    public void unregister() throws Exception;

}
